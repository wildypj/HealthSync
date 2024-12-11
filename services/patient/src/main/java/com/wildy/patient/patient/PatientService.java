package com.wildy.patient.patient;

import com.wildy.patient.payload.PatientDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PatientService {
    List<PatientDTO> getAllPatients();

    Patient getPatientByID(Long id);

    boolean updatePatient(Long id, PatientDTO updatedPatient);

    boolean createPatient(PatientDTO patientDTO);

    boolean deletePatient(Long id);
}
