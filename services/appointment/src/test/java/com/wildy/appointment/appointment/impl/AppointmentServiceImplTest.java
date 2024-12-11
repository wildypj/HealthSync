package com.wildy.appointment.appointment.impl;

import com.wildy.appointment.appointment.Appointment;
import com.wildy.appointment.appointment.AppointmentRepository;
import com.wildy.appointment.appointment.clients.DoctorFeignClient;
import com.wildy.appointment.appointment.clients.PatientFeignClient;
import com.wildy.appointment.appointment.kaf.ScheduleEventConsumer;
import com.wildy.appointment.appointment.payload.AppointmentDTO;
import com.wildy.appointment.appointment.payload.AppointmentMapper;
import com.wildy.appointment.appointment.payload.DoctorDTO;
import com.wildy.appointment.appointment.payload.PatientDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentMapper appointmentMapper;

    @Mock
    private PatientFeignClient patientFeignClient;

    @Mock
    private DoctorFeignClient doctorFeignClient;

    @Mock
    private ScheduleEventConsumer scheduleEventConsumer;

    private AppointmentDTO appointmentDTO;
    private Appointment appointment;

    @BeforeEach
    void setup() {
        appointmentDTO = new AppointmentDTO();
        appointmentDTO.setAppointmentDate(LocalDate.now());
        appointmentDTO.setAppointmentTime(LocalTime.of(9, 30));
        appointmentDTO.setDoctorId(1L);
        appointmentDTO.setPatientId(1L);
        appointmentDTO.setStatus("Scheduled");

        appointment = new Appointment();
        appointment.setId(1L);
        appointment.setAppointmentDate(LocalDate.now());
        appointment.setAppointmentTime(LocalTime.of(9, 30));
        appointment.setDoctorId(1L);
        appointment.setPatientId(1L);
        appointment.setStatus("Scheduled");
    }

    @Test
    void getAllAppointments_returnsListAllAppointments() {
        // Mock repository response
        List<Appointment> appointments = List.of(appointment);
        when(appointmentRepository.findAll()).thenReturn(appointments);
        when(appointmentMapper.convertToDto(appointment)).thenReturn(appointmentDTO);

        // Call the service method
        List<AppointmentDTO> result = appointmentService.getAllAppointments();

        // Verify
        assertEquals(1, result.size());
        assertEquals(appointmentDTO.getAppointmentDate(), result.get(0).getAppointmentDate());
        Mockito.verify(appointmentRepository, times(1)).findAll();
    }

    @Test
    void getAppointmentById_returnAppointment() {
        // Mock repository response
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentMapper.convertToDto(appointment)).thenReturn(appointmentDTO);

        // Call the service method
        AppointmentDTO result = appointmentService.getAppointmentById(1L);

        // Verify
        assertNotNull(result);
        assertEquals(appointmentDTO.getAppointmentDate(), result.getAppointmentDate());
        Mockito.verify(appointmentRepository, times(1)).findById(1L);
    }

    @Test
    void createAppointment_successful() {
        // Mock external service calls
        when(patientFeignClient.getPatientById(1L)).thenReturn(new PatientDTO());
        when(doctorFeignClient.getDoctorById(1L)).thenReturn(new DoctorDTO());
        when(scheduleEventConsumer.isDoctorAvailable(1L, LocalDate.now(), LocalTime.of(9, 30)))
                .thenReturn(true);

        // Mock repository save
        when(appointmentMapper.convertToEntity(appointmentDTO)).thenReturn(new Appointment());
        when(appointmentRepository.save(Mockito.any(Appointment.class))).thenReturn(new Appointment());

        appointmentService.createAppointment(appointmentDTO);

        // Verify save was called
        Mockito.verify(appointmentRepository, times(1)).save(Mockito.any(Appointment.class));
    }

    @Test
    void updateAppointment_successful() {
        // Mock repository and external service calls
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(scheduleEventConsumer.isDoctorAvailable(1L, LocalDate.now(), LocalTime.of(10, 0)))
                .thenReturn(true);

        AppointmentDTO updatedDTO = new AppointmentDTO();
        updatedDTO.setAppointmentDate(LocalDate.now());
        updatedDTO.setAppointmentTime(LocalTime.of(10, 0));
        updatedDTO.setDoctorId(1L);
        updatedDTO.setPatientId(1L);
        updatedDTO.setStatus("Rescheduled");

        appointmentService.updateAppointment(1L, updatedDTO);

        // Verify repository save was called
        Mockito.verify(appointmentRepository, times(1)).save(Mockito.any(Appointment.class));
        assertEquals("Rescheduled", appointment.getStatus());
    }

    @Test
    void deleteAppointment_successful() {
        // Mock repository response
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        appointmentService.deleteAppointment(1L);

        // Verify delete was called
        Mockito.verify(appointmentRepository, times(1)).delete(appointment);
    }
}