package com.wildy.patient.patient.impl;

import com.wildy.patient.address.AddressService;
import com.wildy.patient.patient.Gender;
import com.wildy.patient.patient.Patient;
import com.wildy.patient.patient.PatientRepository;
import com.wildy.patient.payload.PatientDTO;
import com.wildy.patient.payload.PatientMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private AddressService addressService;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    PatientServiceImpl patientServiceImpl;



    @Test
    void PatientService_getAllPatients_ReturnPatientsList() {
        // Arrange
        Patient patient1 = new Patient();
        patient1.setFirstname("John");
        Patient patient2 = new Patient();
        patient2.setFirstname("Jane");

        Mockito.when(patientRepository.findAll()).thenReturn(Arrays.asList(patient1, patient2));

        PatientDTO patientDTO1 = new PatientDTO();
        patientDTO1.setFirstname("John");
        PatientDTO patientDTO2 = new PatientDTO();
        patientDTO2.setFirstname("Jane");

        Mockito.when(patientMapper.convertToDto(patient1)).thenReturn(patientDTO1);
        Mockito.when(patientMapper.convertToDto(patient2)).thenReturn(patientDTO2);
        // Act

        List<PatientDTO> patients = patientServiceImpl.getAllPatients();

        // Assert
        Assertions.assertThat(patients.size()).isEqualTo(2);
        Assertions.assertThat(patients.get(0).getFirstname()).isEqualTo("John");
        Assertions.assertThat(patients.get(1).getFirstname()).isEqualTo("Jane");
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void PatientService_getPatientById_returnPatient() {
        //Arrange
        long patientId = 1L;
        Patient patient = new Patient();
        patient.setFirstname("John");

        Mockito.when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        // Act
        Patient result = patientServiceImpl.getPatientByID(patientId);

        // Assert
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getFirstname()).isEqualTo("John");
        verify(patientRepository, times(1)).findById(patientId);
    }

    @Test
    void PatientService_createPatient_returnTrue_whenPatientNew() {
        //Arrange
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setFirstname("John");
        patientDTO.setLastname("Wick");
        patientDTO.setDob(LocalDate.of(1990, 1, 1));
        patientDTO.setEmail("john.wick@wick.com");
        patientDTO.setPhone("1234567890");
        patientDTO.setGender(Gender.MALE);

        Patient patient = new Patient();
        patient.setEmail(patientDTO.getEmail());
        Mockito.when(patientMapper.convertToEntity(Mockito.any(PatientDTO.class))).thenReturn(patient);
        Mockito.when(patientRepository.findPatientByEmail(patientDTO.getEmail())).thenReturn(Optional.empty());

        // Act
        boolean created = patientServiceImpl.createPatient(patientDTO);

        // Assert
        Assertions.assertThat(created).isTrue();
        // check how many times save has been invoked
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void PatientService_createPatient_returnFalse_whenPatientExist(){
        // Arrange
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setFirstname("John");
        patientDTO.setLastname("Wick");
        patientDTO.setDob(LocalDate.of(1990, 1, 1));
        patientDTO.setEmail("john.wick@wick.com");
        patientDTO.setPhone("1234567890");
        patientDTO.setGender(Gender.MALE);

        Patient patient = new Patient();
        patient.setEmail(patientDTO.getEmail());
        Mockito.when(patientMapper.convertToEntity(Mockito.any(PatientDTO.class))).thenReturn(patient);
        Mockito.when(patientRepository.findPatientByEmail(patientDTO.getEmail())).thenReturn(Optional.of(patient));

        // Act
        boolean created = patientServiceImpl.createPatient(patientDTO);

        // Assert
        assertFalse(created);
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void PatientService_updatePatient_returnTrue_whenPatientExists() {
        //Arrange
        long patientId = 1L;
        Patient existingPatient = new Patient();
        existingPatient.setPatientId(patientId);
        existingPatient.setFirstname("John");

        PatientDTO updatedPatientDTO = new PatientDTO();
        updatedPatientDTO.setFirstname("Anna");
        updatedPatientDTO.setLastname("Armas");
        updatedPatientDTO.setEmail("Anna.Armas@Armas.com");
        updatedPatientDTO.setAddress(new ArrayList<>());
        updatedPatientDTO.setDob(LocalDate.of(1990, 1, 1));

        Mockito.when(patientRepository.findById(patientId)).thenReturn(Optional.of(existingPatient));
        Mockito.when(patientRepository.save(any(Patient.class))).thenReturn(existingPatient);

        //Act
        boolean updated = patientServiceImpl.updatePatient(patientId, updatedPatientDTO);

        //Assert
        Assertions.assertThat(updated).isTrue();
        Assertions.assertThat(existingPatient.getPatientId()).isEqualTo(patientId);
        Assertions.assertThat(existingPatient.getFirstname()).isEqualTo(updatedPatientDTO.getFirstname());
        Assertions.assertThat(existingPatient.getLastname()).isEqualTo(updatedPatientDTO.getLastname());
        Assertions.assertThat(existingPatient.getDob()).isEqualTo(updatedPatientDTO.getDob());
        Assertions.assertThat(existingPatient.getEmail()).isEqualTo(updatedPatientDTO.getEmail());
        verify(patientRepository, times(1)).save(existingPatient);
    }

    @Test
    void PatientServiceImpl_updatePatient_returnFalse_whenPatientNotExist() {
        // Arrange
        long patientId = 1L;
        Mockito.when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        //Act
        boolean updated = patientServiceImpl.updatePatient(patientId, new PatientDTO());


        //Assert
        Assertions.assertThat(updated).isFalse();
        verify(patientRepository, never()).save(any(Patient.class));

    }

    @Test
    void PatientServiceImpl_deletePatient_returnTrue_whenPatientExist() {
        // Arrange
        long patientId = 1L;
        Patient patient = new Patient();
        patient.setPatientId(patientId);
        Mockito.when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        //Act
        boolean deletedPatient = patientServiceImpl.deletePatient(patientId);

        //Assert
        Assertions.assertThat(deletedPatient).isTrue();
        verify(patientRepository, times(1)).deleteById(patientId);
    }

    @Test
    void PatientServiceImpl_deletePatient_shouldReturnFalse_whenPatientNotExist() {
        // Arrange
        long patientId = 1L;
        Mockito.when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        //Act
        boolean deletedPatient = patientServiceImpl.deletePatient(patientId);

        //Assert
        Assertions.assertThat(deletedPatient).isFalse();
        verify(patientRepository, never()).deleteById(any());
    }
}
