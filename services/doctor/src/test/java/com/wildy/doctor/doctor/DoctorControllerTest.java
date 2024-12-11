package com.wildy.doctor.doctor;

import com.wildy.doctor.payload.DoctorAdminDTO;
import com.wildy.doctor.payload.DoctorPublicDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorService;

    @Test
    void DoctorController_getDoctors_returnDoctorsList() throws Exception {
        DoctorPublicDTO doctor1 = new DoctorPublicDTO();
        doctor1.setFirstname("John");
        doctor1.setLastname("Wick");

        DoctorPublicDTO doctor2 = new DoctorPublicDTO();
        doctor2.setFirstname("Jane");
        doctor2.setLastname("Wick");

        List<DoctorPublicDTO> doctors = Arrays.asList(doctor1, doctor2);
        Mockito.when(doctorService.getAllDoctors()).thenReturn(doctors);

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk());
    }

    @Test
    void DoctorController_getDoctorById_returnDoctor() throws Exception {
        long doctorId = 1L;
        Doctor doctor = new Doctor();
        doctor.setFirstname("John");
        doctor.setLastname("Wick");
        Mockito.when(doctorService.getDoctorByID(doctorId)).thenReturn(doctor);

        mockMvc.perform(get("/api/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value(doctor.getFirstname()));
    }

    @Test
    void DoctorController_getDoctorById_returnNotFound() throws Exception {
        long doctorId = 1L;
        Mockito.when(doctorService.getDoctorByID(doctorId)).thenReturn(null);

        mockMvc.perform(get("/api/doctors/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void DoctorController_searchDoctors_returnDoctors() throws Exception {
        // Arrange
        String searchQuery = "John";
        DoctorPublicDTO doctor = new DoctorPublicDTO();
        doctor.setFirstname("John");
        doctor.setLastname("Wick");

        List<DoctorPublicDTO> doctors = Arrays.asList(doctor);
        Mockito.when(doctorService.searchDoctors(searchQuery)).thenReturn(doctors);

        mockMvc.perform(get("/api/doctors/search")
                .param("query", searchQuery))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstname").value(doctor.getFirstname()));

    }

    @Test
    void DoctorController_searchDoctors_returnNotFound() throws Exception {
        String searchQuery = "John";

        List<DoctorPublicDTO> doctors = new ArrayList<>();
        Mockito.when(doctorService.searchDoctors(searchQuery)).thenReturn(doctors);

        mockMvc.perform(get("/api/doctors/search")
                .param("query", searchQuery))
                .andExpect(status().isNoContent());
    }

    @Test
    void DoctorController_updateDoctor_returnStatusOk() throws Exception {
        long doctorId = 1L;

        Mockito.when(doctorService.updateDoctor(eq(doctorId), any(DoctorAdminDTO.class))).thenReturn(true);

        mockMvc.perform(put("/api/doctors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstname\": \"John\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Doctor has successfully updated"));
    }

    @Test
    void DoctorController_updateDoctor_returnNotFound() throws Exception {
        long doctorId = 1L;

        Mockito.when(doctorService.updateDoctor(eq(doctorId), any(DoctorAdminDTO.class))).thenReturn(false);

        mockMvc.perform(put("/api/doctors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstname\": \"John\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Doctor not found"));

    }



    @Test
    void DoctorController_createDoctor_returnStatusCreated() throws Exception {
        // Arrange
        Mockito.when(doctorService.createDoctor(any(DoctorAdminDTO.class))).thenReturn(true);

        mockMvc.perform(post("/api/doctors")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"new_doctor@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Doctor created successfully"));
    }

    @Test
    void DoctorController_createdDoctor_returnStatusConflict() throws Exception {
        //Arrange
        Mockito.when(doctorService.createDoctor(any(DoctorAdminDTO.class))).thenReturn(false);

        mockMvc.perform(post("/api/doctors")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"existing_doctor@example.com\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Doctor with this email already exists"));
    }

    @Test
    void DoctorController_deleteDoctor_returnStatusOk() throws Exception {
        //Arrange
        long doctorId = 1L;
        Mockito.when(doctorService.deleteDoctor(doctorId)).thenReturn(true);

        mockMvc.perform(delete("/api/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Doctor deleted Successfully"));
    }

    @Test
    void DoctorController_deleteDoctor_returnStatusNotFound() throws Exception {
        long doctorId = 1L;
        Mockito.when(doctorService.deleteDoctor(doctorId)).thenReturn(false);

        mockMvc.perform(delete("/api/doctors/1"))
                .andExpect(status().isNotFound());
    }
}