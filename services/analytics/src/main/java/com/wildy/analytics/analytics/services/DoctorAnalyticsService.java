package com.wildy.analytics.analytics.services;

import com.wildy.analytics.analytics.entity.Appointment;
import com.wildy.analytics.analytics.repository.AnalyticsRepository;
import com.wildy.analytics.clients.DoctorFeignClient;
import com.wildy.analytics.clients.PatientFeignClient;
import com.wildy.analytics.kaf.payload.AppointmentEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DoctorAnalyticsService {

    private final AnalyticsRepository analyticsRepository;

    // call open feign clients
    private final DoctorFeignClient doctorFeignClient;
    private final PatientFeignClient patientFeignClient;

    public void saveAppointmentWithDoctorDetails(Long doctorId, AppointmentEventDTO event){
        // call doctor service and get specialty
        String specialty = doctorFeignClient.getDoctorById(doctorId).getSpecialty();

        // create appointment
        Appointment newAppointment = new Appointment();
        newAppointment.setDoctorId(event.getDoctorId());
        newAppointment.setPatientId(event.getPatientId());
        newAppointment.setSpecialty(specialty);
        newAppointment.setStatus(event.getStatus());


        analyticsRepository.save(newAppointment);
    }

    public Map<String, Long> calculateSpecialtyPopularity() {
        return analyticsRepository.countBySpecialty();
    }
}
