package com.wildy.doctor.payload;

import com.wildy.doctor.address.Address;
import com.wildy.doctor.doctor.Specialization;
import com.wildy.doctor.officeAddress.OfficeAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAdminDTO {
    private String firstname;
    private String lastname;
    private Specialization specialization;
    private String email;
    private String phone;
    private LocalDate dob;
    private List<Address> address = new ArrayList<>();
    private OfficeAddress officeAddresses;
}
// add past patient list for patients that book
//    private String password;  -- add this field later for security
