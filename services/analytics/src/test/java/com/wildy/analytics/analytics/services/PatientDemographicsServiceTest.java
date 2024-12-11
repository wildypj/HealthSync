package com.wildy.analytics.analytics.services;

import com.wildy.analytics.analytics.entity.PatientDemographics;
import com.wildy.analytics.analytics.repository.PatientDemographicsRepository;
import com.wildy.analytics.clients.PatientFeignClient;
import com.wildy.analytics.clients.dto.Gender;
import com.wildy.analytics.clients.dto.PatientDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientDemographicsServiceTest {
    @Mock
    private PatientDemographicsRepository demographicsRepository;

    @Mock
    private PatientFeignClient patientFeignClient;

    @InjectMocks
    private PatientDemographicsService patientDemographicsService;

    @Test
    void updatePatientDemographics() {
        PatientDTO patient = new PatientDTO();
        patient.setFirstname("John");
        patient.setLastname("Doe");
        patient.setDob(LocalDate.of(1985, 1, 1));
        patient.setGender(Gender.MALE);
        patient.setEmail("test@gmail.com");
        when(patientFeignClient.getPatientById(2L)).thenReturn(patient);

        patientDemographicsService.updatePatientDemographics(2L);

        verify(demographicsRepository, times(1)).save(Mockito.any(PatientDemographics.class));
    }

    @Test
    void calculateAge() {
        LocalDate dob = LocalDate.of(2000, 1, 1);
        int age = patientDemographicsService.calculateAge(dob);
        assertEquals(24, age);
    }

    @Test
    void getPatientDemographics() {
        // Simulating a response from the demographicsRepository
        Map<String, Long> demographics = Map.of("Male", 100L, "Female", 120L);
        when(demographicsRepository.countByGender()).thenReturn(demographics);

        // Calling the method and verifying the result
        Map<String, Long> result = patientDemographicsService.getPatientDemographics();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(100L, result.get("Male"));
        assertEquals(120L, result.get("Female"));
    }
}