package com.wildy.analytics.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsAggregateDTO {

    private double repeatPatientRate;  // from RepeatPatientRateService
    private Map<String, Long> specialtyPopularity; // from DoctorAnalyticsService
    private Map<String, Long> patientDemographics; // map of gender and count or age groups and counts
}
