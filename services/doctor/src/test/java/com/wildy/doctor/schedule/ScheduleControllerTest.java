package com.wildy.doctor.schedule;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScheduleController.class)
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleService scheduleService;


    @Test
    void getAllSchedules() throws Exception {
        // Arrange
        when(scheduleService.getAllDoctorSchedules(1L)).thenReturn(List.of(new Schedule()));

        // Act & Assert
        mockMvc.perform(get("/api/1/schedule"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getSchedulesByDate() throws Exception {
        // Arrange
        Schedule schedule = new Schedule();
        schedule.setId(1L);
        schedule.setDate(LocalDate.now());
        schedule.setStartTime(LocalTime.of(10, 0));
        schedule.setEndTime(LocalTime.of(11, 0));

        when(scheduleService.getDoctorScheduleByDate(anyLong(), any(LocalDate.class)))
                .thenReturn(List.of(schedule));

        // Act & Assert
        mockMvc.perform(get("/api/1/schedule/date?date=2024-11-19"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void createSchedule() throws Exception {

        Long doctorId = 1L;

        // Arrange
        Schedule newSchedule = new Schedule();
        newSchedule.setDate(LocalDate.now());
        newSchedule.setStartTime(LocalTime.of(10, 0));
        newSchedule.setEndTime(LocalTime.of(11, 0));

        // Mock the service method to return a Schedule object, not a ResponseEntity
        Schedule savedSchedule = new Schedule(); // create a mock Schedule object
        savedSchedule.setDate(newSchedule.getDate());
        savedSchedule.setStartTime(newSchedule.getStartTime());
        savedSchedule.setEndTime(newSchedule.getEndTime());

        Mockito.when(scheduleService.createSchedule(Mockito.eq(doctorId), Mockito.any(Schedule.class)))
                .thenReturn(savedSchedule); // Return a Schedule, not a ResponseEntity

        // Act & Assert
        mockMvc.perform(post("/api/1/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"date\": \"2024-11-19\", \"startTime\": \"10:00\", \"endTime\": \"11:00\" }"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Schedule created successfully"));
    }

    @Test
    void updateSchedule() throws Exception {
        // Arrange
        Schedule updatedSchedule = new Schedule();
        updatedSchedule.setStartTime(LocalTime.of(9, 0));
        updatedSchedule.setEndTime(LocalTime.of(10, 0));

        doNothing().when(scheduleService).updateSchedule(anyLong(), anyLong(), any(Schedule.class));

        // Act & Assert
        mockMvc.perform(put("/api/1/schedule/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"startTime\": \"09:00\", \"endTime\": \"10:00\" }"))
                .andExpect(status().isOk())
                .andExpect(content().string("Schedule updated successfully"));
    }

    @Test
    void deleteSchedule() throws Exception {
        // Arrange
        doNothing().when(scheduleService).deleteSchedule(anyLong(), anyLong());

        // Act & Assert
        mockMvc.perform(delete("/api/1/schedule/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Schedule deleted successfully"));
    }


}