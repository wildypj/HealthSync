package com.wildy.doctor.address.impl;

import com.wildy.doctor.address.Address;
import com.wildy.doctor.address.AddressRepository;
import com.wildy.doctor.doctor.Doctor;
import com.wildy.doctor.doctor.DoctorRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private AddressServiceImpl addressService;


    @Test
    void addDoctorAddress() {
        Address address = new Address();
        address.setStreet("456 Elm Street");

        Doctor doctor = new Doctor();
        doctor.setFirstname("John");
        doctor.setLastname("Wick");
        doctor.setAddress(new ArrayList<>());

        Mockito.when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
//        Mockito.when(addressRepository.save(address)).thenReturn(address);
        Mockito.doAnswer(invocation -> {
            doctor.getAddress().add(address); // Simulate the address being added to the doctor's list
            return null;
        }).when(addressRepository).save(Mockito.any(Address.class));

        //Act
        boolean result = addressService.addDoctorAddress(1L, address);

        Assertions.assertThat(result).isTrue();
        Assertions.assertThat(doctor.getAddress().size()).isEqualTo(1);
        Mockito.verify(doctorRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(addressRepository, Mockito.times(1)).save(address);
    }

    @Test
    void addDoctorAddress_returnFalse_WhenDoctorNotFound() {
        Mockito.when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        Address address = new Address();
        address.setStreet("456 Elm Street");

        boolean result = addressService.addDoctorAddress(1L, address);
        Assertions.assertThat(result).isFalse();
        Mockito.verify(doctorRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(addressRepository, Mockito.never()).save(address);
    }

    @Test
    void addDoctorAddress_returnFalse_WhenAddressListIsFull() {
        // Create existing address
        Address address1 = new Address();
        Address address2 = new Address();
        Address address3 = new Address();
        Address address4 = new Address();

        //Add exsiting address to the doctor
        Doctor doctor = new Doctor();
        doctor.setFirstname("John");
        doctor.setLastname("Wick");
        doctor.setAddress(new ArrayList<>(List.of(address1, address2, address3, address4)));

        //New Address
        Address address5 = new Address();
        address5.setStreet("456 Elm Street");

        Mockito.when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        boolean result = addressService.addDoctorAddress(1L, address5);
        Assertions.assertThat(result).isFalse();
        Assertions.assertThat(doctor.getAddress().size()).isEqualTo(4);


    }

    @Test
    void deleteDoctorAddress_returnTrue_whenDeletedSuccessfully() {
        Doctor doctor = new Doctor();
        doctor.setDoctorId(1L);

        Address address = new Address();
        address.setAddressId(10L);
        address.setDoctor(doctor);

        Mockito.when(addressRepository.findById(10L)).thenReturn(Optional.of(address));

        boolean result = addressService.deleteDoctorAddress(1L, 10L);

        Assertions.assertThat(result).isTrue();
        Mockito.verify(addressRepository, Mockito.times(1)).findById(10L);
        Mockito.verify(addressRepository, Mockito.times(1)).deleteById(10L);
    }

    @Test
    void deleteDoctorAddress_returnFalse_whenDeletedNotSuccessful() {
        Doctor doctor = new Doctor();
        doctor.setDoctorId(1L);

        Mockito.when(addressRepository.findById(10L)).thenReturn(Optional.empty());

        boolean result = addressService.deleteDoctorAddress(1L, 10L);
        Assertions.assertThat(result).isFalse();
    }
}