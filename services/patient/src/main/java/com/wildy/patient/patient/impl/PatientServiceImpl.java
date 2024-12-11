package com.wildy.patient.patient.impl;

import com.wildy.patient.address.Address;
import com.wildy.patient.address.AddressRepository;
import com.wildy.patient.address.AddressService;
import com.wildy.patient.patient.Patient;
import com.wildy.patient.patient.PatientRepository;
import com.wildy.patient.patient.PatientService;
import com.wildy.patient.payload.PatientDTO;
import com.wildy.patient.payload.PatientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    // inject repository
    private final PatientRepository patientRepository;
    private final AddressRepository addressRepository;
    private final AddressService addressService;
    private final PatientMapper patientMapper;


    @Override
    public List<PatientDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();

        return patients.stream()
                .map(patientMapper:: convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Patient getPatientByID(Long id) {
        return patientRepository.findById(id).orElse(null);
    }



    @Override
    public boolean createPatient(PatientDTO patientDTO) {
        Patient patient = patientMapper.convertToEntity(patientDTO);

        //first check if patient already exist in db by email
        Optional<Patient> existingPatient = patientRepository.findPatientByEmail(patient.getEmail());

        if (existingPatient.isPresent()) {
            return false;
        }

        // Save the patient first to generate an ID
        patientRepository.save(patient);

        // Patient created successfully
        return true;
    }

    @Override
    public boolean updatePatient(Long id, PatientDTO updatedPatientDTO) {
        //check if patient exist in db
        Patient patientExist = patientRepository.findById(id).orElse(null);
        if(patientExist == null) {
            return false;
        }

        // update patient fields
        updatePatientFields(patientExist, updatedPatientDTO);

        patientRepository.save(patientExist);
        return true;
    }

    private void updatePatientFields(Patient existingPatient, PatientDTO updatedPatientDTO) {
        existingPatient.setFirstname(updatedPatientDTO.getFirstname());
        existingPatient.setLastname(updatedPatientDTO.getLastname());
        existingPatient.setDob(updatedPatientDTO.getDob());
        existingPatient.setGender(updatedPatientDTO.getGender());
        existingPatient.setEmail(updatedPatientDTO.getEmail());
        existingPatient.setPhone(updatedPatientDTO.getPhone());
    }


    @Override
    public boolean deletePatient(Long id) {
        Patient patient = patientRepository.findById(id).orElse(null);
        if(patient == null) {
            return false;
        }

        patientRepository.deleteById(patient.getPatientId());
        return true;
    }
}