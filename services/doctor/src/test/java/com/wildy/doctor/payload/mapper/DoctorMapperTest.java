package com.wildy.doctor.payload.mapper;

import com.wildy.doctor.doctor.Doctor;
import com.wildy.doctor.doctor.Specialization;
import com.wildy.doctor.payload.DoctorAdminDTO;
import com.wildy.doctor.payload.DoctorPublicDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoctorMapperTest {

    private final DoctorMapper doctorMapper = new DoctorMapper();


    @Test
    void DoctorMapper_toPublicDTO() {
        //Arrange
        Doctor doctor = new Doctor();
        doctor.setFirstname("John");
        doctor.setLastname("Wick");
        doctor.setSpecialization(Specialization.CARDIOLOGY);
        doctor.setEmail("john.wick@wildy.com");
        //Act
        DoctorPublicDTO publicDTO = doctorMapper.toPublicDTO(doctor);

        assertEquals(doctor.getFirstname(), publicDTO.getFirstname());
        assertEquals(doctor.getLastname(), publicDTO.getLastname());
        assertEquals(doctor.getSpecialization(), publicDTO.getSpecialization());
    }

    @Test
    void toAdminDTO() {
        Doctor doctor = new Doctor();
        doctor.setFirstname("John");
        doctor.setLastname("Wick");
        doctor.setSpecialization(Specialization.CARDIOLOGY);
        doctor.setEmail("john.wick@wildy.com");

        // Act
        DoctorAdminDTO adminDTO = doctorMapper.toAdminDTO(doctor);

        //Act
        assertEquals(doctor.getFirstname(), adminDTO.getFirstname());
        assertEquals(doctor.getLastname(), adminDTO.getLastname());
        assertEquals(doctor.getSpecialization(), adminDTO.getSpecialization());
        assertEquals(doctor.getEmail(), adminDTO.getEmail());
    }

    @Test
    void toDoctorEntity() {
        DoctorAdminDTO doctorAdminDTO = new DoctorAdminDTO();
        doctorAdminDTO.setFirstname("John");
        doctorAdminDTO.setLastname("Wick");
        doctorAdminDTO.setEmail("john.wick@wildy.com");
        doctorAdminDTO.setSpecialization(Specialization.CARDIOLOGY);

        //Act
        Doctor doctor = doctorMapper.toDoctorEntity(doctorAdminDTO);

        assertEquals(doctor.getFirstname(), doctorAdminDTO.getFirstname());
        assertEquals(doctor.getLastname(), doctorAdminDTO.getLastname());
        assertEquals(doctor.getEmail(), doctorAdminDTO.getEmail());
        assertEquals(doctor.getSpecialization(), doctorAdminDTO.getSpecialization());
    }
}