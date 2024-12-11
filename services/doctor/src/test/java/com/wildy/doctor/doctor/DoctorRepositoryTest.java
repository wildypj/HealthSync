package com.wildy.doctor.doctor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class DoctorRepositoryTest {

    @Autowired
    private DoctorRepository doctorRepository;

    private Doctor doctor;

    @BeforeEach
    public void setUp() {
        doctor = new Doctor();
        doctor.setFirstname("John");
        doctor.setLastname("Wick");
        doctor.setEmail("john.wick@example.com");
        doctor.setPhone("1234567890");
        doctor.setSpecialization(Specialization.CARDIOLOGY);
    }

    @Test
    void findDoctorByEmail() {
        // Arrange
        doctorRepository.save(doctor);

        // Act
        Optional<Doctor> foundEmail = doctorRepository.findDoctorByEmail(doctor.getEmail());

        //Assert
        Assertions.assertThat(foundEmail).isNotNull();
        Assertions.assertThat(foundEmail.get().getEmail()).isEqualTo(doctor.getEmail());

    }

    @Test
    void findBySpecializationLike() {
        //  Arrange
        doctorRepository.save(doctor);

        //  Act
        List<Doctor> result = doctorRepository.findBySpecializationLike("cardio");


        //  Assert
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void findByNameLike() {
        //Arrange
        doctorRepository.save(doctor);

        // Act
        List<Doctor> result =  doctorRepository.findByNameLike("John");

        //Assert
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(1);
    }
}