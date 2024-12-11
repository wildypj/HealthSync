package com.wildy.patient.address.impl;

import com.wildy.patient.address.Address;
import com.wildy.patient.address.AddressRepository;
import com.wildy.patient.patient.Patient;
import com.wildy.patient.patient.PatientRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void AddressServiceImpl_addPatientAddress_returnTrue_whenPatientExistsAndHasLessThanTwoAddresses() {
////
//        // Arrange
//        Address address = new Address();
//        address.setStreet("456 Elm Street");
//
//        // Create a patient with no addresses yet
//        Patient patient = new Patient();
//        patient.setFirstname("John");
//        patient.setLastname("Wick");
//        patient.setAddress(new ArrayList<>());
//
//
//        Mockito.when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
//        // Mock patient retrieval
//        Mockito.doAnswer(invocation -> {
//            patient.getAddress().add(address); // Simulate the address being added to the doctor's list
//            return null;
//        }).when(addressRepository).save(Mockito.any(Address.class));
//
//        // Act
//        boolean result = addressService.addPatientAddress(1L, address);
//
//        // Assert
//        Assertions.assertThat(result).isTrue();  // Ensure the result is true
//        Assertions.assertThat(patient.getAddress()).hasSize(1);
//        verify(addressRepository, times(1)).save(any(Address.class));  // Verify save was called

        // Arrange
        Address address = new Address();
        address.setStreet("456 Elm Street");

        Patient patient = new Patient();
        patient.setFirstname("John");
        patient.setLastname("Wick");
        patient.setAddress(new ArrayList<>()); // Ensure initialized list

        // Mock patient retrieval
        Mockito.when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        // Mock save behavior
        Mockito.when(addressRepository.save(any(Address.class))).thenAnswer(invocation -> {
            Address savedAddress = invocation.getArgument(0);
            patient.getAddress().add(savedAddress); // Add address to patient's address list
            return savedAddress;
        });

        // Act
        boolean result = addressService.addPatientAddress(1L, address);

        // Assert
        Assertions.assertThat(result).isTrue();  // Ensure the result is true
        Assertions.assertThat(patient.getAddress()).hasSize(1); // Ensure patient has exactly one address
        verify(addressRepository, times(1)).save(any(Address.class));
    }


    @Test
    void deletePatientAddress() {
        // Arrange
        Long patientId = 1L;
        Long addressId = 10L;

        Patient patient = new Patient();
        patient.setPatientId(patientId);

        Address address = new Address();
        address.setAddressId(addressId);
        address.setPatient(patient);

        // Mock address retrieval to return empty (address doesn't exist)
        Mockito.when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

        // Act
        boolean result = addressService.deletePatientAddress(patientId, addressId);

        // Assert
        Assertions.assertThat(result).isTrue();  // The address does not exist, so return false

    }
}