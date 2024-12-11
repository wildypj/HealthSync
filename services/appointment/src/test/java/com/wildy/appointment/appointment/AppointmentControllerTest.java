package com.wildy.appointment.appointment;

import com.wildy.appointment.appointment.payload.AppointmentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    private AppointmentDTO testAppointment;

    @BeforeEach
    void setUp() {
        testAppointment = new AppointmentDTO();
        testAppointment.setAppointmentDate(LocalDate.now());
        testAppointment.setAppointmentTime(LocalTime.of(10, 0));
        testAppointment.setStatus("Scheduled");
        testAppointment.setPatientId(1L);
        testAppointment.setDoctorId(2L);
    }

    @Test
    void getAppointments() throws Exception {
        when(appointmentService.getAllAppointments())
                .thenReturn(Collections.singletonList(testAppointment));

        mockMvc.perform(get("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAppointmentById() throws Exception {
        when(appointmentService.getAppointmentById(anyLong()))
                .thenReturn(testAppointment);

        mockMvc.perform(get("/api/v1/appointments/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createAppointment() throws Exception {
        Mockito.doNothing().when(appointmentService).createAppointment(Mockito.any(AppointmentDTO.class));

        mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "appointmentDate": "2024-11-20",
                            "appointmentTime": "10:00",
                            "status": "Scheduled",
                            "patientId": 1,
                            "doctorId": 2
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(content().string("Appointment created successfully"));
    }

    @Test
    void updateAppointment() throws Exception {
        Mockito.doNothing().when(appointmentService).updateAppointment(anyLong(), Mockito.any(AppointmentDTO.class));

        mockMvc.perform(put("/api/v1/appointments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "appointmentDate": "2024-11-21",
                            "appointmentTime": "14:00",
                            "status": "Rescheduled",
                            "patientId": 1,
                            "doctorId": 2
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(content().string("Appointment updated successfully"));
    }

    @Test
    void deleteAppointment() throws Exception {
        Mockito.doNothing().when(appointmentService).deleteAppointment(anyLong());

        mockMvc.perform(delete("/api/v1/appointments/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Appointment Canceled successfully"));
    }
}