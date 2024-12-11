package com.wildy.doctor.address.impl;

import com.wildy.doctor.address.Address;
import com.wildy.doctor.address.AddressRepository;

import com.wildy.doctor.address.AddressService;
import com.wildy.doctor.doctor.Doctor;
import com.wildy.doctor.doctor.DoctorRepository;
import com.wildy.doctor.officeAddress.OfficeAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final DoctorRepository doctorRepository;

    // P
    @Override
    public boolean addDoctorAddress(Long doctorId,Address address) {
        //check if the doctor exist by ID
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);

        if(doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();

            //check if patient already have 4 addresses
            if(doctor.getAddress().size() >= 4){
                return false;
            }

            address.setDoctor(doctor);
            addressRepository.save(address);
            return true;
        }

        return false;
    }

    @Override
    public boolean deleteDoctorAddress(Long doctorId, Long addressId) {
       Optional<Address> addressExist = addressRepository.findById(addressId);

       //check if address exist in db
       if(addressExist.isPresent() && addressExist.get().getDoctor().getDoctorId().equals(doctorId)) {
           addressRepository.deleteById(addressId);
           return true;
       }
        return false;
    }
}
