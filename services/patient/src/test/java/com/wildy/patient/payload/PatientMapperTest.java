package com.wildy.patient.payload;

import com.wildy.patient.patient.Gender;
import com.wildy.patient.patient.Patient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PatientMapperTest {

    private final PatientMapper patientMapper = new PatientMapper();

    @Test
    public void PatientMapper_convertToDto_ShouldMapEntityToDto() {
        // Arrange
        Patient patient = new Patient();
        patient.setFirstname("John");
        patient.setLastname("Wick");
        patient.setDob(LocalDate.of(1990, 1, 1));
        patient.setEmail("john.wick@wick.com");
        patient.setPhone("1234567890");
        patient.setGender(Gender.MALE);

        // Act
        PatientDTO patientDTO = patientMapper.convertToDto(patient);

        // Assert
        Assertions.assertEquals("John", patientDTO.getFirstname());
        Assertions.assertEquals("Wick", patientDTO.getLastname());
        Assertions.assertEquals(LocalDate.of(1990, 1, 1), patientDTO.getDob());
        Assertions.assertEquals("john.wick@wick.com", patientDTO.getEmail());
        Assertions.assertEquals("1234567890", patientDTO.getPhone());
        Assertions.assertEquals(Gender.MALE, patientDTO.getGender());
    }

    @Test
    public void PatientMapper_convertToEntity_ShouldMapDtoToEntity() {
        // Arrange
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setFirstname("John");
        patientDTO.setLastname("Wick");
        patientDTO.setDob(LocalDate.of(1990, 1, 1));
        patientDTO.setEmail("john.wick@wick.com");
        patientDTO.setPhone("1234567890");
        patientDTO.setGender(Gender.MALE);

        // Act
        Patient patient = patientMapper.convertToEntity(patientDTO);

        // Assert
        Assertions.assertEquals(patientDTO.getFirstname(), patient.getFirstname());
        Assertions.assertEquals(patientDTO.getLastname(), patient.getLastname());
        Assertions.assertEquals(patientDTO.getDob(), patient.getDob());
        Assertions.assertEquals(patientDTO.getEmail(), patient.getEmail());
        Assertions.assertEquals(patientDTO.getPhone(), patient.getPhone());
        Assertions.assertEquals(patientDTO.getGender(), patient.getGender());
    }
}