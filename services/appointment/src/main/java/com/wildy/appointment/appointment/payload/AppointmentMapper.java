package com.wildy.appointment.appointment.payload;

import com.wildy.appointment.appointment.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    // convert to dto
    public AppointmentDTO convertToDto(Appointment appointment) {
        AppointmentDTO appointmentDTO = new AppointmentDTO();

        appointmentDTO.setAppointmentDate(appointment.getAppointmentDate());
        appointmentDTO.setAppointmentTime(appointment.getAppointmentTime());
        appointmentDTO.setStatus(appointment.getStatus());
        appointmentDTO.setPatientId(appointmentDTO.getPatientId());
        appointmentDTO.setDoctorId(appointmentDTO.getDoctorId());
        return appointmentDTO;
    }

    // convert to entity
    public Appointment convertToEntity(AppointmentDTO appointmentDTO) {
        Appointment appointment = new Appointment();

        appointment.setAppointmentDate(appointmentDTO.getAppointmentDate());
        appointment.setAppointmentTime(appointmentDTO.getAppointmentTime());
        appointment.setStatus(appointmentDTO.getStatus());
        appointment.setPatientId(appointmentDTO.getPatientId());
        appointment.setDoctorId(appointmentDTO.getDoctorId());
        return appointment;
    }
}

