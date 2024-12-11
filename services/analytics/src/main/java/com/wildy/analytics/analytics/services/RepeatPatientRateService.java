package com.wildy.analytics.analytics.services;

import com.wildy.analytics.analytics.entity.Appointment;
import com.wildy.analytics.analytics.repository.AnalyticsRepository;
import com.wildy.analytics.kaf.payload.AppointmentEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RepeatPatientRateService {

    private final AnalyticsRepository analyticsRepository;

    public void updateAppointmentRecord(AppointmentEventDTO event) {
        Appointment newAppointment = new Appointment();
        newAppointment.setDoctorId(event.getDoctorId());
        newAppointment.setPatientId(event.getPatientId());
        newAppointment.setStatus(event.getStatus());

        analyticsRepository.save(newAppointment);
    }

    public double calculateRepeatPatientRate() {
        long totalPatients = analyticsRepository.countDistinctPatientIds();
        long repeatPatients = analyticsRepository.countRepeatPatientIds();
        return (double) repeatPatients / totalPatients * 100;
    }
}
