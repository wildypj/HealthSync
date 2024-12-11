package com.wildy.doctor.doctor.impl;

import com.wildy.doctor.doctor.Doctor;
import com.wildy.doctor.doctor.DoctorRepository;
import com.wildy.doctor.payload.DoctorAdminDTO;
import com.wildy.doctor.payload.DoctorPublicDTO;
import com.wildy.doctor.payload.mapper.DoctorMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

//import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorMapper doctorMapper;

    @InjectMocks
    private DoctorServiceImpl doctorService;


    @Test
    void getAllDoctors() {
        //Arrange
        Doctor doctor = new Doctor();
        doctor.setFirstname("John");
        doctor.setLastname("Wick");

        Doctor doctor2 = new Doctor();
        doctor2.setFirstname("Jane");
        doctor2.setLastname("Wick");

        Mockito.when(doctorRepository.findAll()).thenReturn(List.of(doctor, doctor2));

        //Act
        List<DoctorPublicDTO> result = doctorService.getAllDoctors();

        //Assert
        Assertions.assertThat(result).hasSize(2);
    }

    @Test
    void getDoctorByID() {
        //Arrange
        Doctor doctor = new Doctor();
        doctor.setDoctorId(1L);
        doctor.setFirstname("John");
        doctor.setLastname("Wick");

        Mockito.when(doctorRepository.findById(doctor.getDoctorId())).thenReturn(Optional.of(doctor));

        //Act
        var result = doctorService.getDoctorByID(1L);

        //Assert
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getFirstname()).isEqualTo("John");
        Mockito.verify(doctorRepository, Mockito.times(1)).findById(doctor.getDoctorId());
    }

    @Test
    void DoctorServiceImpl_searchDoctors_returnDoctorListBySearchNames() {
        //Arrange
        Doctor doctor = new Doctor();
        doctor.setFirstname("John");
        doctor.setLastname("Wick");

        Mockito.when(doctorRepository.findByNameLike("john")).thenReturn(List.of(doctor));

        //Act
        List<DoctorPublicDTO> result = doctorService.searchDoctors("john");

        //Assert
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result).hasSize(1);
    }



    @Test
    void DoctorServiceImpl_createDoctor_returnTrue_whenDoctorNew() {
        //Arrange
        DoctorAdminDTO doctorAdminDTO = new DoctorAdminDTO();
        doctorAdminDTO.setEmail("John.Wick@test.com");

        Doctor doctor = new Doctor();
        doctor.setFirstname("John");
        doctor.setLastname("Wick");
        //mock db
        Mockito.when(doctorRepository.findDoctorByEmail(doctorAdminDTO.getEmail())).thenReturn(Optional.empty());
        Mockito.when(doctorMapper.toDoctorEntity(doctorAdminDTO)).thenReturn(doctor);
        Mockito.when(doctorRepository.save(doctor)).thenReturn(doctor);

        //Act
        boolean result = doctorService.createDoctor(doctorAdminDTO);

        //Assert
        Assertions.assertThat(result).isTrue();
    }

    @Test
    void DoctorServiceImpl_createDoctor_returnFalse_whenDoctorExists() {
        DoctorAdminDTO doctorAdminDTO = new DoctorAdminDTO();
        doctorAdminDTO.setEmail("John.Wick@test.com");

        Doctor existingDoctor = new Doctor();
        existingDoctor.setEmail("John.Wick@test.com");
        Mockito.when(doctorRepository.findDoctorByEmail(doctorAdminDTO.getEmail())).thenReturn(Optional.of(existingDoctor));

        // Act
        boolean result = doctorService.createDoctor(doctorAdminDTO);

        //Assert
        Assertions.assertThat(result).isFalse();
        Mockito.verify(doctorRepository, Mockito.times(0)).save(any(Doctor.class));
    }

    @Test
    void DoctorServiceImpl_updateDoctor_returnTrue_whenDoctorUpdate() {
        //Arrange
        /// updated data
        DoctorAdminDTO doctorAdminDTO = new DoctorAdminDTO();
        doctorAdminDTO.setFirstname("John");
        doctorAdminDTO.setLastname("updated");

        //existing data
        Doctor existingDoctor = new Doctor();
        existingDoctor.setFirstname("John");
        existingDoctor.setLastname("wick");

        Mockito.when(doctorRepository.findById(1L)).thenReturn(Optional.of(existingDoctor));
        Mockito.when(doctorRepository.save(existingDoctor)).thenReturn(existingDoctor);

        //Act
        boolean result = doctorService.updateDoctor(1L, doctorAdminDTO);

        //Assert
        Assertions.assertThat(result).isTrue();
        Assertions.assertThat(existingDoctor.getLastname()).isEqualTo("updated");
        Mockito.verify(doctorRepository, Mockito.times(1)).save(existingDoctor);
    }

    @Test
    void DoctorServiceImpl_updateDoctor_returnFalse_whenDoctorNotFound() {
        //Arrange
        DoctorAdminDTO doctorAdminDTO = new DoctorAdminDTO();
        doctorAdminDTO.setFirstname("John");
        doctorAdminDTO.setLastname("wick");

        Mockito.when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        //Act
        boolean result = doctorService.updateDoctor(1L, doctorAdminDTO);

        //Assert
        Assertions.assertThat(result).isFalse();
        verify(doctorRepository, Mockito.times(0)).save(any());
    }



    @Test
    void DoctorServiceImpl_deleteDoctor_returnTrue_whenDoctorDeleted() {
        //Arrange
        Doctor doctor = new Doctor();
        doctor.setFirstname("John");
        doctor.setLastname("Wick");
        Mockito.when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        Mockito.doNothing().when(doctorRepository).delete(doctor);

        //Act
        boolean result = doctorService.deleteDoctor(1L);

        //Assert
        Assertions.assertThat(result).isTrue();
        verify(doctorRepository, Mockito.times(1)).delete(doctor);
    }

    @Test
    void DoctorServiceImpl_deleteDoctor_returnFalse_whenDoctorNotFound() {
        //Arrange
        Doctor doctor = new Doctor();
        doctor.setFirstname("John");
        doctor.setLastname("Wick");
        Mockito.when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        //Act
        boolean result = doctorService.deleteDoctor(1L);

        //Assert
        Assertions.assertThat(result).isFalse();
    }
}