package com.wildy.doctor.doctor;

import com.wildy.doctor.payload.DoctorAdminDTO;
import com.wildy.doctor.payload.DoctorPublicDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    //get all doctors
    @GetMapping
    public ResponseEntity<List<DoctorPublicDTO>> getDoctors() {
        List<DoctorPublicDTO> publicDTOs = doctorService.getAllDoctors();
        return ResponseEntity.ok(publicDTOs);
    }

    // might only be used internally strictly
    //get doctor by id
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorByID(id);

        return doctor != null
                ? ResponseEntity.ok(doctor)
                : ResponseEntity.notFound().build();
    }

    // endpoint for public
    // search doctor by name and
    @GetMapping("/search")
    public ResponseEntity<List<DoctorPublicDTO>> searchDoctors(@RequestParam String query) {

        List<DoctorPublicDTO> publicDTOs = doctorService.searchDoctors(query);
        if (publicDTOs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(publicDTOs);
        }
        return ResponseEntity.ok(publicDTOs);
    }

    //Update a doctor by id or by email
    @PutMapping("/{id}")
    public Object updateDoctor(
            @PathVariable Long id,
            @RequestBody DoctorAdminDTO updatedDoctor) {

        return doctorService.updateDoctor(id, updatedDoctor)
                ? ResponseEntity.ok("Doctor has successfully updated")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
    }

    //Create a doctor
    @PostMapping
    public ResponseEntity<String> createDoctor(@RequestBody DoctorAdminDTO doctorAdminDTO) {
        return doctorService.createDoctor(doctorAdminDTO)
                ? ResponseEntity.status(HttpStatus.CREATED).body("Doctor created successfully")
                : ResponseEntity.status(HttpStatus.CONFLICT).body("Doctor with this email already exists");
    }

    //Delete a doctor by ID or by email
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long id) {
        return doctorService.deleteDoctor(id)
                ? ResponseEntity.ok("Doctor deleted Successfully")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}


//update


