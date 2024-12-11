package com.wildy.analytics.kaf.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentEventDTO {
    private Long patientId;
    private Long doctorId;
    private String appointmentType; // e.g., Created, Confirmed, Canceled
    private String status; // Notification message // e.g., CREATED, UPDATED, CANCELED
}
