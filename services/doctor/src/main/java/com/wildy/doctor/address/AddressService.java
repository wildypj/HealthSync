package com.wildy.doctor.address;

import org.springframework.stereotype.Service;

@Service
public interface AddressService {

    boolean deleteDoctorAddress(Long doctorId,Long addressId);

    boolean addDoctorAddress(Long doctorId,Address address);
}
