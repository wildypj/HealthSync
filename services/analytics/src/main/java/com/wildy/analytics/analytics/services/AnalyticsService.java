package com.wildy.analytics.analytics.services;

import com.wildy.analytics.kaf.payload.AppointmentEventDTO;
import com.wildy.analytics.payload.AnalyticsAggregateDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final DoctorAnalyticsService doctorAnalyticsService;
    private final PatientDemographicsService patientDemographicsService;
    private final RepeatPatientRateService repeatPatientRateService;

    public AnalyticsAggregateDTO getAggregatedAnalytics() {
        // Fetch the individual data
        double repeatPatientRate = repeatPatientRateService.calculateRepeatPatientRate();
        Map<String, Long> specialtyPopularity = doctorAnalyticsService.calculateSpecialtyPopularity();
        Map<String, Long> patientDemographics = patientDemographicsService.getPatientDemographics();

        // Return them as a single DTO
        return new AnalyticsAggregateDTO(repeatPatientRate, specialtyPopularity, patientDemographics);
    }
    @Transactional
    public void processAppointmentEvent(AppointmentEventDTO event) {
        // You can decide on the exact actions you want to take
        // For example, saving the appointment record and updating relevant services

        // Save the appointment with doctor details
        doctorAnalyticsService.saveAppointmentWithDoctorDetails(event.getDoctorId(), event);

        // Update patient demographics (if needed)
        patientDemographicsService.updatePatientDemographics(event.getPatientId());

        // Update the repeat patient rate
        repeatPatientRateService.updateAppointmentRecord(event);
    }
}
