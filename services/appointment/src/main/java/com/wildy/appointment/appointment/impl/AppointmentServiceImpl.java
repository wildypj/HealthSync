package com.wildy.appointment.appointment.impl;

import com.wildy.appointment.appointment.Appointment;
import com.wildy.appointment.appointment.AppointmentRepository;
import com.wildy.appointment.appointment.AppointmentService;
import com.wildy.appointment.appointment.clients.DoctorFeignClient;
import com.wildy.appointment.appointment.clients.PatientFeignClient;
import com.wildy.appointment.appointment.kaf.ScheduleEventConsumer;
import com.wildy.appointment.appointment.payload.AppointmentDTO;
import com.wildy.appointment.appointment.payload.AppointmentMapper;
import com.wildy.appointment.appointment.payload.DoctorDTO;
import com.wildy.appointment.appointment.payload.PatientDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    //Injections
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final PatientFeignClient patientFeignClient;
    private final DoctorFeignClient doctorFeignClient;
    // Inject the Kafka Consumer
    private final ScheduleEventConsumer scheduleEventConsumer;


    @Override
    public List<AppointmentDTO> getAllAppointments() {
        //get all of the appointments in db
        List<Appointment> appointments = appointmentRepository.findAll();

        // Use the mapper to convert the list to DTOs
        return appointments.stream()
                .map(appointmentMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Appointment not found " + id));

        return appointmentMapper.convertToDto(appointment);
    }

    @Override
    public void createAppointment(AppointmentDTO appointmentDTO) {
        //check if the patient and doctor exist using feign clients
        PatientDTO patient = patientFeignClient.getPatientById(appointmentDTO.getPatientId());
        DoctorDTO doctor = doctorFeignClient.getDoctorById(appointmentDTO.getDoctorId());

        if(patient == null){
            throw new IllegalArgumentException("Patient not found " + appointmentDTO.getPatientId());
        }
        if(doctor == null){
            throw new IllegalArgumentException("Doctor not found " + appointmentDTO.getDoctorId());
        }

        // Check doctor availability
        // Check doctor availability using ScheduleEventConsumer
        boolean isAvailable = scheduleEventConsumer.isDoctorAvailable(
                appointmentDTO.getDoctorId(),
                appointmentDTO.getAppointmentDate(),
                appointmentDTO.getAppointmentTime()
        );
        if (!isAvailable) {
            throw new IllegalArgumentException("Doctor is not available at the selected time");
        }

        // convert dto to entity and save in database
        Appointment appointment = appointmentMapper.convertToEntity(appointmentDTO);
        appointmentRepository.save(appointment);
    }

    @Override
    public void updateAppointment(Long appointmentId, AppointmentDTO updatedAppointmentDTO) {
        // check db if appointment in db
        Appointment existingAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + appointmentId));

        // Check doctor availability for the new date and time
        boolean isAvailable = scheduleEventConsumer.isDoctorAvailable(
                updatedAppointmentDTO.getDoctorId(),
                updatedAppointmentDTO.getAppointmentDate(),
                updatedAppointmentDTO.getAppointmentTime()
        );

        if (!isAvailable) {
            throw new IllegalArgumentException("Doctor is not available at the selected time");
        }

        // Update the appointment details
        existingAppointment.setAppointmentDate(updatedAppointmentDTO.getAppointmentDate());
        existingAppointment.setAppointmentTime(updatedAppointmentDTO.getAppointmentTime());
        existingAppointment.setStatus(updatedAppointmentDTO.getStatus());

        // Save the updated appointment
        appointmentRepository.save(existingAppointment);
    }

    @Override
    public void deleteAppointment(Long appointmentId) {
        // Check if the appointment exists
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + appointmentId));

        // Delete the appointment
        appointmentRepository.delete(appointment);
    }
}
