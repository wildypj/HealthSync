package com.wildy.patient.patient;

import com.wildy.patient.payload.PatientDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PatientController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false) // bypass security
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @Test
    void PatientController_getPatients_returnPatientsList() throws Exception {
        //Arrange
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setFirstname("John");
        Mockito.when(patientService.getAllPatients()).thenReturn(Collections.singletonList(patientDTO));

        //Act & Assert
        mockMvc.perform(get("/api/v1/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstname").value("John"));
    }

    @Test
    void PatientController_getPatientById_returnPatient() throws Exception {
        // Arrange
        long patientId = 1L;
        Patient patient = new Patient();
        patient.setPatientId(patientId);
        patient.setFirstname("John");
        Mockito.when(patientService.getPatientByID(patient.getPatientId())).thenReturn(patient);

        //Act & Assert
        mockMvc.perform(get("/api/v1/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value(patient.getFirstname()));// Use "$.firstname" for a single object
    }

    @Test
    void PatientController_getPatientById_returnPatientNotFound_whenPatientNotFound() throws Exception {
        long patientId = 1L;
        Mockito.when(patientService.getPatientByID(patientId)).thenReturn(null);

        mockMvc.perform(get("/api/v1/patients/1"))
                .andExpect(status().isNotFound());
    }


    // Update Test
    @Test
    void PatientController_updatePatient_returnStatusOK_whenSuccessful() throws Exception {
        // Arrange
        long patientId = 1L;
        Mockito.when(patientService.updatePatient(eq(patientId), any())).thenReturn(true);

        mockMvc.perform(put("/api/v1/patients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstname\": \"UpdatedName\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("UpdatedName"));
    }

    @Test
    void PatientController_updatePatient_returnStatusNotFound_whenPatientNotFound() throws Exception {
        long patientId = 1L;
        Mockito.when(patientService.updatePatient(eq(patientId), any())).thenReturn(false);

        // Create a valid JSON payload for the request
        String patientJson = """
        {
            "firstname": "John",
            "lastname": "Doe",
            "age": 30
        }
    """;

        mockMvc.perform(put("/api/v1/patients/1")
                .contentType(MediaType.APPLICATION_JSON).content(patientJson))
                // Ensure valid JSON body
                .andExpect(status().isNotFound());
    }


    // Create Test
    @Test
    void PatientController_createPatient_ReturnCreatedStatus_whenSuccessful() throws Exception {
        // Arrange
        Mockito.when(patientService.createPatient(any())).thenReturn(true);

        //Act & Assert
        mockMvc.perform(post("/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"new_patient@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Patient created successfully"));
    }

    @Test
    void PatientController_createPatient_returnStatusConflict_whenPatientAlreadyExists() throws Exception {
        // Arrange
        Mockito.when(patientService.createPatient(any())).thenReturn(false);

        mockMvc.perform(post("/api/v1/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"existing_patient@example.com\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Patient with this email already exists"));
    }


    //Delete patient
    @Test
    void PatientController_deletePatient_returnStatusOk_whenSuccessful() throws Exception {
        long patientId = 1L;
        Mockito.when(patientService.deletePatient(patientId)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/patients/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Patient deleted successfully"));
    }

    @Test
    void PatientController_deletePatient_returnStatusNotFound_whenPatientNotFound() throws Exception {
        long patientId = 1L;
        Mockito.when(patientService.deletePatient(patientId)).thenReturn(false);

        //Assert
        mockMvc.perform(delete("/api/v1/patients/1"))
                .andExpect(status().isNotFound());
    }
}