package com.wildy.analytics.clients.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorDTO {
    private Long id; // Doctor ID
    private String firstname; // Doctor's first name
    private String lastname;  // Doctor's last name
    private String specialty; // Doctor's specialty
}
