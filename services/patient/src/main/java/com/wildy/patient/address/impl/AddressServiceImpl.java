package com.wildy.patient.address.impl;

import com.wildy.patient.address.Address;
import com.wildy.patient.address.AddressRepository;
import com.wildy.patient.address.AddressService;
import com.wildy.patient.patient.Patient;
import com.wildy.patient.patient.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final PatientRepository patientRepository;

    // Patient
    @Override
    public boolean addPatientAddress(Long patientId ,Address address) {
        //check if the patient exist by Id
        Optional<Patient> patientOpt = patientRepository.findById(patientId);

        if(patientOpt.isPresent()) {
            Patient patient = patientOpt.get();

            //check if patient already have two addresses
            if(patient.getAddress().size() >= 2){
                return false;
            }

            address.setPatient(patient);
            addressRepository.save(address);
            return true;
        }

        return false;
    }

    @Override
    public boolean deletePatientAddress(Long patientId ,Long addressId) {
       Optional<Address> addressExist = addressRepository.findById(addressId);

       //check if address exist in db
       if(addressExist.isPresent() && addressExist.get().getPatient().getPatientId().equals(patientId)) {
           addressRepository.deleteById(addressId);
           return true;
       }

        return false;
    }
}
