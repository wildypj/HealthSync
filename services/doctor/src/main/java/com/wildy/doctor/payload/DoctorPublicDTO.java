package com.wildy.doctor.payload;

import com.wildy.doctor.doctor.Specialization;
import com.wildy.doctor.officeAddress.OfficeAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorPublicDTO {
    private String firstname;
    private String lastname;
    private Specialization specialization;
    private OfficeAddress officeAddress;
}

// maybe create a payload for schedule
// or just include schedule into
