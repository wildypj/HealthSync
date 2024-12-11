package com.wildy.appointment.appointment.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentEventDTO {
    private Long patientId;           // ID of the patient involved in the appointment
    private Long doctorId;            // ID of the doctor involved in the appointment
    private String appointmentType;   // Type of appointment event (e.g., "Created", "Updated", "Canceled")
    private String status;
}
