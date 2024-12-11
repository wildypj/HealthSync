package com.wildy.doctor.payload.mapper;

import com.wildy.doctor.doctor.Doctor;
import com.wildy.doctor.payload.DoctorAdminDTO;
import com.wildy.doctor.payload.DoctorPublicDTO;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

    // Convert Doctor to DoctorPublicDTO
    public DoctorPublicDTO toPublicDTO(Doctor doctor) {
        //create new Doctor Public instance
        DoctorPublicDTO dto = new DoctorPublicDTO();

        dto.setFirstname(doctor.getFirstname());
        dto.setLastname(doctor.getLastname());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setOfficeAddress(doctor.getOfficeAddresses());
        return dto;
    }

    // Convert Doctor to DoctorAdminDTO
    public DoctorAdminDTO toAdminDTO(Doctor doctor) {
        DoctorAdminDTO dto = new DoctorAdminDTO();

        dto.setFirstname(doctor.getFirstname());
        dto.setLastname(doctor.getLastname());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setEmail(doctor.getEmail());
        dto.setPhone(doctor.getPhone());
        dto.setDob(doctor.getDob());
        dto.setAddress(doctor.getAddress());
        dto.setOfficeAddresses(doctor.getOfficeAddresses());
        return dto;
    }

    // Convert DoctorAdminDTO to Doctor
    public Doctor toDoctorEntity (DoctorAdminDTO doctorDTO) {
        if (doctorDTO == null) {
            return null;
        }

        Doctor doctor = new Doctor();
        doctor.setFirstname(doctorDTO.getFirstname());
        doctor.setLastname(doctorDTO.getLastname());
        doctor.setEmail(doctorDTO.getEmail());
        doctor.setPhone(doctorDTO.getPhone());
        doctor.setDob(doctorDTO.getDob());
        doctor.setSpecialization(doctorDTO.getSpecialization());
        doctor.setAddress(doctorDTO.getAddress());
        doctor.setOfficeAddresses(doctorDTO.getOfficeAddresses());

        return doctor;
    }
}
