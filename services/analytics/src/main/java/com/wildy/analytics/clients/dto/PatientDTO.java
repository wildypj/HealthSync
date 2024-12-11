package com.wildy.analytics.clients.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PatientDTO {
    private String firstname;
    private String lastname;
    private LocalDate dob;
    private Gender gender;
    private String email;
}
