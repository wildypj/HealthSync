package com.wildy.doctor.doctor;

import com.wildy.doctor.payload.DoctorAdminDTO;
import com.wildy.doctor.payload.DoctorPublicDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DoctorService {
    List<DoctorPublicDTO> getAllDoctors();

    Doctor getDoctorByID(Long id);

    boolean createDoctor(DoctorAdminDTO doctorAdminDTO);

    boolean updateDoctor(Long id, DoctorAdminDTO updatedDoctor);

    boolean deleteDoctor(Long id);

    List<DoctorPublicDTO> searchDoctors(String query);
}
