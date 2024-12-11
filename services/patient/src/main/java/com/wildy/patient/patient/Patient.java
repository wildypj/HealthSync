package com.wildy.patient.patient;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wildy.patient.address.Address;
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
@Table(name = "patient")
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long patientId;

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
    private Gender gender;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 15)
    private String phone;

    @JsonManagedReference
    @OneToMany(mappedBy = "patient",cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Address> address = new ArrayList<>();
}

// to add later when implementing security
//    @NotBlank
//    @Size(min = 8, max = 120)
//    @Column(name = "password")
//    private String password;
