package com.wildy.patient.patient;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;



@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Test
    void findPatientByEmail() {
        // Arrange
        Patient patient = new Patient();
        patient.setFirstname("John");
        patient.setLastname("Doe");
        patient.setEmail("john@doe.com");
        patient.setPhone("1234567890");
        patient.setGender(Gender.MALE);
        patientRepository.save(patient);
        // Act

        Patient foundEmail = patientRepository.findPatientByEmail(patient.getEmail()).get();
        //Assert

        Assertions.assertThat(foundEmail).isNotNull();
        Assertions.assertThat(foundEmail.getEmail()).isEqualTo(patient.getEmail());
    }
}