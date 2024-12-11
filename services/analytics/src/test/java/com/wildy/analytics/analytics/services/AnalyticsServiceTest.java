package com.wildy.analytics.analytics.services;

import com.wildy.analytics.kaf.payload.AppointmentEventDTO;
import com.wildy.analytics.payload.AnalyticsAggregateDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private DoctorAnalyticsService doctorAnalyticsService;
    @Mock
    private PatientDemographicsService patientDemographicsService;
    @Mock
    private RepeatPatientRateService repeatPatientRateService;

    @InjectMocks
    private AnalyticsService analyticsService;

    @Test
    void getAggregatedAnalytics() {
        Map<String, Long> specialtyPopularity = Map.of("Cardiology", 5L);
        Map<String, Long> demographics = Map.of("Male", 10L, "Female", 15L);
        double repeatRate = 25.0;

        when(doctorAnalyticsService.calculateSpecialtyPopularity()).thenReturn(specialtyPopularity);
        when(patientDemographicsService.getPatientDemographics()).thenReturn(demographics);
        when(repeatPatientRateService.calculateRepeatPatientRate()).thenReturn(repeatRate);

        AnalyticsAggregateDTO result = analyticsService.getAggregatedAnalytics();

        assertEquals(repeatRate, result.getRepeatPatientRate());
        assertEquals(specialtyPopularity, result.getSpecialtyPopularity());
        assertEquals(demographics, result.getPatientDemographics());
    }

    @Test
    void processAppointmentEvent() {
        AppointmentEventDTO event = new AppointmentEventDTO();
        event.setDoctorId(1L);
        event.setPatientId(2L);
        event.setStatus("CREATED");

        analyticsService.processAppointmentEvent(event);

        verify(doctorAnalyticsService, times(1)).saveAppointmentWithDoctorDetails(1L, event);
        verify(patientDemographicsService, times(1)).updatePatientDemographics(2L);
        verify(repeatPatientRateService, times(1)).updateAppointmentRecord(event);
    }
}