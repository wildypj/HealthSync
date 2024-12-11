package com.wildy.patient.patient;

import com.wildy.patient.payload.PatientDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    //inject service
    private final PatientService patientService;

    // Get all patients
    // admin endpoint
    @GetMapping
    public ResponseEntity<List<PatientDTO>> getPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    // get patient by id
    // admin endpoint
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        //new ResponseEntity<>(service.getAllPatients(), HttpStatus.OK);
        Patient patient = patientService.getPatientByID(id);
        if(patient == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(patient);
    }

    //update patient through id
    // admin endpoint for patient
    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(
            @PathVariable Long id,
            @RequestBody PatientDTO updatedPatientDTO) {

        return patientService.updatePatient(id, updatedPatientDTO)
                ? ResponseEntity.ok(updatedPatientDTO)
                : ResponseEntity.notFound().build();
    }


    // create new patient
    // admin endpoint
    @PostMapping
    public ResponseEntity<String> createPatient(@RequestBody PatientDTO patientDTO) {
        return patientService.createPatient(patientDTO)
                ? ResponseEntity.status(HttpStatus.CREATED).body("Patient created successfully")
                : ResponseEntity.status(HttpStatus.CONFLICT).body("Patient with this email already exists");
    }


    //  Delete patient by id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable Long id) {
        return patientService.deletePatient(id)
                ? ResponseEntity.ok("Patient deleted successfully")
                : ResponseEntity.notFound().build();
    }
}
//TODO
// later add a doctor list that patient has visited before
// maybe just bundle it and call it previous visit history



// need to link through patient profile
// retrieve medical history by patient profiles
