package com.wildy.doctor.officeAddress;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wildy.doctor.doctor.Doctor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "office_addresses")
@AllArgsConstructor
@NoArgsConstructor
public class OfficeAddress {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long officeAddressId;
    @NotBlank
    @Size(min = 5, message = "Street name must be at least 5 characters")
    private String street;

    @NotBlank
    @Size(min = 4, message = "City name must be at least 4 characters")
    private String city;

    @NotBlank
    @Size(min = 2, message = "State name must be at least 2 characters")
    private String state;

    @NotBlank
    @Size(min = 2, message = "Country name must be at least 2 characters")
    private String country;

    @NotBlank
    @Size(min = 5, message = "Zip code must be at least 5 characters")
    @Pattern(regexp = "^[0-9]{5}(-[0-9]{4})?$", message = "Zip code must be a valid format (e.g., 12345 or 12345-6789)")
    private String zipCode;

    @OneToOne
    @JoinColumn(name = "doctor_id")
    @JsonBackReference
    private Doctor doctor; // Foreign key to the Doctor entity
}
