package com.wildy.doctor.officeAddress.impl;

import com.wildy.doctor.doctor.Doctor;
import com.wildy.doctor.doctor.DoctorRepository;
import com.wildy.doctor.officeAddress.OfficeAddress;
import com.wildy.doctor.officeAddress.OfficeAddressRepository;
import com.wildy.doctor.officeAddress.OfficeAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfficeAddressServiceImpl implements OfficeAddressService {
    private final OfficeAddressRepository officeAddressRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public boolean addDoctorOffice(Long doctorId, OfficeAddress officeAddress) {
        // find doctor by id
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            // Set the doctor in the office address
            officeAddress.setDoctor(doctor);
            // Save the office address
            officeAddressRepository.save(officeAddress);
            return true;
        }
        return false; // Doctor not found
    }

    @Override
    public boolean updateOfficeAddress(Long doctorId, Long updateOfficeId, OfficeAddress updateOfficeAddress) {
        // Check if the office address exists
        Optional<OfficeAddress> optionalAddress = officeAddressRepository.findById(updateOfficeId);

        if (optionalAddress.isPresent() && optionalAddress.get().getDoctor().getDoctorId().equals(doctorId)) {
            OfficeAddress existingAddress = optionalAddress.get();
            // Update fields as necessary
            existingAddress.setStreet(updateOfficeAddress.getStreet());
            existingAddress.setCity(updateOfficeAddress.getCity());
            existingAddress.setState(updateOfficeAddress.getState());
            existingAddress.setCountry(updateOfficeAddress.getCountry());
            existingAddress.setZipCode(updateOfficeAddress.getZipCode());
            // Save the updated address
            officeAddressRepository.save(existingAddress);
            return true;
        }
        return false; // Address not found or does not belong to the specified doctor
    }

    @Override
    public boolean deleteDoctorOffice(Long doctorId, Long officeId) {
        // Check if the office address exists
        Optional<OfficeAddress> optionalAddress = officeAddressRepository.findById(officeId);

        if (optionalAddress.isPresent() && optionalAddress.get().getDoctor().getDoctorId().equals(doctorId)) {
            officeAddressRepository.deleteById(officeId);
            return true;
        }
        return false;
    }
}
