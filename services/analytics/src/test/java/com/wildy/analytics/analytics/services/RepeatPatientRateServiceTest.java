package com.wildy.analytics.analytics.services;

import com.wildy.analytics.analytics.entity.Appointment;
import com.wildy.analytics.analytics.repository.AnalyticsRepository;
import com.wildy.analytics.kaf.payload.AppointmentEventDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepeatPatientRateServiceTest {

    @Mock
    private AnalyticsRepository analyticsRepository;

    @InjectMocks
    private RepeatPatientRateService repeatPatientRateService;

    @Test
    void updateAppointmentRecord() {
        // Create a mock AppointmentEventDTO
        AppointmentEventDTO event = new AppointmentEventDTO();
        event.setDoctorId(1L);
        event.setPatientId(2L);
        event.setStatus("CREATED");

        // Call the method to be tested
        repeatPatientRateService.updateAppointmentRecord(event);

        // Verify that the save method was called on the repository
        verify(analyticsRepository, times(1)).save(Mockito.any(Appointment.class));
    }

    @Test
    void calculateRepeatPatientRate() {
        when(analyticsRepository.countDistinctPatientIds()).thenReturn(100L);
        when(analyticsRepository.countRepeatPatientIds()).thenReturn(25L);

        double rate = repeatPatientRateService.calculateRepeatPatientRate();
        assertEquals(25.0, rate);
    }
}