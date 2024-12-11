package com.wildy.doctor.doctor.impl;

import com.wildy.doctor.doctor.Doctor;
import com.wildy.doctor.doctor.DoctorRepository;
import com.wildy.doctor.doctor.DoctorService;
import com.wildy.doctor.payload.DoctorAdminDTO;
import com.wildy.doctor.payload.DoctorPublicDTO;
import com.wildy.doctor.payload.mapper.DoctorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;


    @Override
    public List<DoctorPublicDTO> getAllDoctors() {
        // get all doctors
        List<Doctor> doctor =doctorRepository.findAll();

        // map all of them to public entity
        return doctor.stream()
                .map(doctorMapper::toPublicDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Doctor getDoctorByID(Long id) {
        return doctorRepository.findById(id).orElse(null);
    }

    @Override
    public List<DoctorPublicDTO> searchDoctors(String query) {
        List<Doctor> bySpecialization = doctorRepository.findBySpecializationLike(query);
        List<Doctor> byName = doctorRepository.findByNameLike(query);

        // Combine results and remove duplicates
        bySpecialization.addAll(byName);
        List<Doctor> combinedDoctors = bySpecialization.stream().distinct().toList();

        //Map to DTO
        return combinedDoctors.stream()
                .map(doctorMapper::toPublicDTO)
                .collect(Collectors.toList());
    }

    //TODO update this function similar to the patients createPatient
    @Override
    public boolean createDoctor(DoctorAdminDTO doctorAdminDTO) {
        //check if doctor exist in db
        Optional<Doctor> doctorExist = doctorRepository.findDoctorByEmail(doctorAdminDTO.getEmail());

        if(doctorExist.isPresent()) {
            return false;
        }
        Doctor doctor = doctorMapper.toDoctorEntity(doctorAdminDTO);
        doctorRepository.save(doctor);

        return true;
    }

    @Override
    public boolean updateDoctor(Long id, DoctorAdminDTO updatedDoctorDTO) {
        //check if doctor exists in db
        Doctor doctorExist = doctorRepository.findById(id).orElse(null);

        if (doctorExist == null) {
            return false;
        }

        //function to update doctor fields
        updateDoctorFields(doctorExist, updatedDoctorDTO);

        doctorRepository.save(doctorExist);
        return true;
    }

    public void updateDoctorFields(Doctor existingDoctor, DoctorAdminDTO updatedDoctor) {
        existingDoctor.setFirstname(updatedDoctor.getFirstname());
        existingDoctor.setLastname(updatedDoctor.getLastname());
        existingDoctor.setDob(updatedDoctor.getDob());
        existingDoctor.setEmail(updatedDoctor.getEmail());
        existingDoctor.setPhone(updatedDoctor.getPhone());
    }

    @Override
    public boolean deleteDoctor(Long id) {
        //check if in db first
        Doctor doctor = doctorRepository.findById(id).orElse(null);
        if(doctor == null) {
            return false;
        }

        doctorRepository.delete(doctor);
        return true;
    }
}

