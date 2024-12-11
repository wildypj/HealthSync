package com.wildy.patient.payload;

import com.wildy.patient.address.Address;
import com.wildy.patient.patient.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientDTO {
    private String firstname;
    private String lastname;
    private LocalDate dob;
    private Gender gender;
    private String email;
    private String phone;
    private List<Address> address = new ArrayList<>();
}
