package com.wildy.appointment.appointment.payload;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AppointmentDTO {
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String status;

    private Long patientId;
    private Long doctorId;
}
