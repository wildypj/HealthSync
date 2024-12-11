package com.wildy.patient.address;

import org.springframework.stereotype.Service;

@Service
public interface AddressService {

    boolean deletePatientAddress(Long patientId, Long addressId);

    boolean addPatientAddress(Long patientId, Address address);
}
