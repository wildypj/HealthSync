package com.wildy.appointment.appointment.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientDTO {
    private Long id; // Patient ID
    private String firstname; // Patient's first name
    private String lastname;  // Patient's last name
    private String email;     // Patient's email
    private String phone;     // Patient's phone number
}
