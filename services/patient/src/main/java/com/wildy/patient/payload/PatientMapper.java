package com.wildy.patient.payload;

import com.wildy.patient.patient.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    // Convert Patient to PatientDTO
    public PatientDTO convertToDto(Patient patient) {
        PatientDTO patientDTO = new PatientDTO();

        patientDTO.setFirstname(patient.getFirstname());
        patientDTO.setLastname(patient.getLastname());
        patientDTO.setDob(patient.getDob());
        patientDTO.setGender(patient.getGender());
        patientDTO.setEmail(patient.getEmail());
        patientDTO.setPhone(patient.getPhone());
        patientDTO.setAddress(patient.getAddress());
        return patientDTO;
    }

    // Convert PatientDTO to Patient
    public Patient convertToEntity(PatientDTO patientDTO) {
        Patient patient = new Patient();
        patient.setFirstname(patientDTO.getFirstname());
        patient.setLastname(patientDTO.getLastname());
        patient.setDob(patientDTO.getDob());
        patient.setGender(patientDTO.getGender());
        patient.setEmail(patientDTO.getEmail());
        patient.setPhone(patientDTO.getPhone());
        patient.setAddress(patientDTO.getAddress());
        return patient;
    }
}
