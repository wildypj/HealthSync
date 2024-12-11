package com.wildy.patient.address;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wildy.patient.patient.Patient;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Street name must be at least 5 character")
    private String street;

    @NotBlank
    @Size(min = 4, message = "city name must be at least 4 character")
    private String city;

    @NotBlank
    @Size(min = 2, message = "state name must be at least 2 character")
    private String state;

    @NotBlank
    @Size(min = 2, message = "Country name must be at least 2 character")
    private String country;

    @NotBlank
    @Size(min = 5, message = "zipCode name must be at least 2 character")
    private String zipCode;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @JsonBackReference
    private Patient patient;
}
