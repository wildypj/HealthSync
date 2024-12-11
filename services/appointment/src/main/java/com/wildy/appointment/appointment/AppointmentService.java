package com.wildy.appointment.appointment;

import com.wildy.appointment.appointment.payload.AppointmentDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AppointmentService {
    List<AppointmentDTO> getAllAppointments();

    AppointmentDTO getAppointmentById(Long id);

    void createAppointment(AppointmentDTO appointmentDTO);

    void updateAppointment(Long appointmentId,AppointmentDTO updatedAppointmentDTO);

    void deleteAppointment(Long appointmentId);
}
