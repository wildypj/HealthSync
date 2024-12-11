package com.wildy.doctor.doctor;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wildy.doctor.address.Address;
import com.wildy.doctor.officeAddress.OfficeAddress;
import com.wildy.doctor.schedule.Schedule;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "doctor")
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Long doctorId;

    @NotBlank(message = "First name must not be blank")
    @Size(min = 2, max = 20, message = "First name must be between 2 and 20 characters")
    @Column(name = "firstname")
    private String firstname;

    @NotBlank(message = "Last name must not be blank")
    @Size(min = 2, max = 20,  message = "Last name must be between 2 and 20 characters")
    @Column(name = "lastname")
    private String lastname;

    private LocalDate dob;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Specialization specialization;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 15)
    private String phone;

    @JsonManagedReference
    @OneToMany(mappedBy = "doctor",cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Address> address = new ArrayList<>(); // List of home addresses


    @JsonManagedReference
    @OneToOne(mappedBy = "doctor", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private OfficeAddress officeAddresses;

    @JsonManagedReference
    @OneToMany(mappedBy = "doctor")
    private List<Schedule> schedules;
}



//TODO
// need an address for office

//TODO
// need to add password later
// start date of practice -- needed to calculate years of practice