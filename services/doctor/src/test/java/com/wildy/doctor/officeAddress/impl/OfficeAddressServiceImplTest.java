package com.wildy.doctor.officeAddress.impl;

import com.wildy.doctor.doctor.Doctor;
import com.wildy.doctor.doctor.DoctorRepository;
import com.wildy.doctor.officeAddress.OfficeAddress;
import com.wildy.doctor.officeAddress.OfficeAddressRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class OfficeAddressServiceImplTest {
    @Mock
    private OfficeAddressRepository officeAddressRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private OfficeAddressServiceImpl officeAddressService;

    @Test
    void addDoctorOffice() {
        // Arrange
        OfficeAddress officeAddress = new OfficeAddress();
        officeAddress.setStreet("456 Elm Street");

        Long doctorId = 1L;
        Doctor doctor = new Doctor();
        doctor.setDoctorId(doctorId);


        Mockito.when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        Mockito.when(officeAddressRepository.save(Mockito.any(OfficeAddress.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        boolean result = officeAddressService.addDoctorOffice(doctorId, officeAddress);

        //Assert
        Assertions.assertThat(result).isTrue();
        Assertions.assertThat(officeAddress.getDoctor().getDoctorId()).isEqualTo(doctorId);
        Assertions.assertThat(officeAddress.getStreet()).isEqualTo("456 Elm Street");
        Mockito.verify(doctorRepository, Mockito.times(1)).findById(doctorId);
        Mockito.verify(officeAddressRepository, Mockito.times(1)).save(Mockito.any(OfficeAddress.class));
    }

    @Test
    void addDoctorOffice_returnFalse(){
        //Arrange
        OfficeAddress officeAddress = new OfficeAddress();
        officeAddress.setStreet("456 Elm Street");
        Long doctorId = 1L;
        Doctor doctor = new Doctor();
        doctor.setDoctorId(doctorId);
        Mockito.when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        //Act
        boolean result = officeAddressService.addDoctorOffice(doctorId, officeAddress);

        //Assert
        Assertions.assertThat(result).isFalse();
        Mockito.verify(doctorRepository, Mockito.times(1)).findById(doctorId);
        Mockito.verify(officeAddressRepository, Mockito.never()).save(Mockito.any(OfficeAddress.class));
    }

    @Test
    void updateOfficeAddress() {
        //Arrange
        Long doctorId = 1L;
        Long officeId = 1L;
        OfficeAddress existingAddress = new OfficeAddress();
        existingAddress.setOfficeAddressId(officeId);
        existingAddress.setStreet("Old Street");
        Doctor doctor = new Doctor();
        doctor.setDoctorId(doctorId);
        existingAddress.setDoctor(doctor);

        OfficeAddress updatedAddress = new OfficeAddress();
        updatedAddress.setStreet("New Street");
        updatedAddress.setCity("Updated City");
        updatedAddress.setState("Updated State");
        updatedAddress.setCountry("Updated Country");
        updatedAddress.setZipCode("54321");

        Mockito.when(officeAddressRepository.findById(officeId)).thenReturn(Optional.of(existingAddress));
        Mockito.when(officeAddressRepository.save(Mockito.any(OfficeAddress.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        boolean result = officeAddressService.updateOfficeAddress(doctorId, officeId, updatedAddress);

        // Assert
        assertTrue(result, "The office address should be updated successfully");
        assertEquals("New Street", existingAddress.getStreet(), "The street should be updated");
        assertEquals("Updated City", existingAddress.getCity(), "The city should be updated");
        assertEquals("Updated State", existingAddress.getState(), "The state should be updated");
        assertEquals("Updated Country", existingAddress.getCountry(), "The country should be updated");
        assertEquals("54321", existingAddress.getZipCode(), "The zip code should be updated");
        Mockito.verify(officeAddressRepository, Mockito.times(1)).save(existingAddress);
    }

    @Test
    void deleteDoctorOffice_returnTrue() {
        //Arrange
        Long doctorId = 1L;
        Long officeAddressId = 2L;

        Doctor doctor = new Doctor();
        doctor.setDoctorId(doctorId);
        OfficeAddress officeAddress = new OfficeAddress();
        officeAddress.setOfficeAddressId(officeAddressId);
        officeAddress.setDoctor(doctor);

        Mockito.when(officeAddressRepository.findById(officeAddressId)).thenReturn(Optional.of(officeAddress));

        //Act
        boolean result = officeAddressService.deleteDoctorOffice(doctorId, officeAddressId);

        //Assert
        Assertions.assertThat(result).isTrue();
        Assertions.assertThat(officeAddress.getDoctor().getDoctorId()).isEqualTo(doctorId);
        Mockito.verify(officeAddressRepository, Mockito.times(1)).findById(officeAddressId);
        Mockito.verify(officeAddressRepository, Mockito.times(1)).deleteById(officeAddressId);
    }

    @Test
    void deleteDoctorOffice_returnFalse_dontBelongToDoctor(){
        //Arrange
        Long doctorId = 1L;
        Long officeAddressId = 2L;

        OfficeAddress officeAddress = new OfficeAddress();
        officeAddress.setOfficeAddressId(officeAddressId);
        Doctor doctor = new Doctor();
        doctor.setDoctorId(3L);
        officeAddress.setDoctor(doctor);

        Mockito.when(officeAddressRepository.findById(officeAddressId)).thenReturn(Optional.of(officeAddress));

        //Act
        boolean result = officeAddressService.deleteDoctorOffice(doctorId, officeAddressId);

        //Assert
        Assertions.assertThat(result).isFalse();
        Mockito.verify(officeAddressRepository, Mockito.times(1)).findById(officeAddressId);
        Mockito.verify(officeAddressRepository, never()).deleteById(officeAddressId);
    }
}