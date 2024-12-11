package com.wildy.analytics.analytics.services;

import com.wildy.analytics.analytics.entity.PatientDemographics;
import com.wildy.analytics.analytics.repository.PatientDemographicsRepository;
import com.wildy.analytics.clients.PatientFeignClient;
import com.wildy.analytics.clients.dto.PatientDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PatientDemographicsService {

    // call doctor patient microservices
    private final PatientFeignClient patientFeignClient;
    private final PatientDemographicsRepository demographicsRepository;

    public void updatePatientDemographics(Long patientId){
        PatientDTO patient = patientFeignClient.getPatientById(patientId);

        //create new PatientDemographics
        PatientDemographics newPatientDemographics = new PatientDemographics();
        newPatientDemographics.setPatientId(patientId);
        newPatientDemographics.setGender(patient.getGender());
        newPatientDemographics.setAge(calculateAge(patient.getDob()));

        demographicsRepository.save(newPatientDemographics);
    }

    public int calculateAge(LocalDate dob){
        return Period.between(dob, LocalDate.now()).getYears();
    }
    public Map<String, Long> getPatientDemographics() {
        Map<String, Long> demographics = demographicsRepository.countByGender(); // Or use countByAgeGroup(), depending on your use case
        return demographics;
    }
}
