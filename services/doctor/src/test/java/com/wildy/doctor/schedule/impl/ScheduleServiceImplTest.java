package com.wildy.doctor.schedule.impl;

import com.wildy.doctor.doctor.Doctor;
import com.wildy.doctor.doctor.DoctorRepository;
import com.wildy.doctor.kaf.ScheduleEventDTO;
import com.wildy.doctor.kaf.ScheduleEventProducer;
import com.wildy.doctor.schedule.Schedule;
import com.wildy.doctor.schedule.ScheduleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ScheduleEventProducer eventProducer;

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    @Test
    void createSchedule_successfullyPublished() {
        // Arrange
        Doctor doctor = new Doctor();
        doctor.setDoctorId(1L);

        Schedule newSchedule = new Schedule();
        newSchedule.setDate(LocalDate.now());
        newSchedule.setStartTime(LocalTime.of(10, 0));
        newSchedule.setEndTime(LocalTime.of(11, 0));

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(scheduleRepository.findByDoctorAndDate(doctor, newSchedule.getDate())).thenReturn(Collections.emptyList());
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(newSchedule);

        // Act
        Schedule result = scheduleService.createSchedule(1L, newSchedule);

        // Assert
        assertNotNull(result);
        verify(eventProducer).publishScheduleEvent(any(ScheduleEventDTO.class));
    }

    @Test
    void getDoctorScheduleByDate_successful() {
        Doctor doctor = new Doctor();
        doctor.setDoctorId(1L);

        List<Schedule> schedules = List.of(new Schedule());
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(scheduleRepository.findByDoctorAndDate(doctor, LocalDate.now())).thenReturn(schedules);

        // Act
        List<Schedule> result = scheduleService.getDoctorScheduleByDate(1L, LocalDate.now());

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void getAllDoctorSchedules_successful() {
        // Arrange
        Doctor doctor = new Doctor();
        doctor.setDoctorId(1L);

        List<Schedule> schedules = List.of(new Schedule());
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(scheduleRepository.findByDoctor(doctor)).thenReturn(schedules);

        // Act
        List<Schedule> result = scheduleService.getAllDoctorSchedules(1L);

        // Assert
        assertEquals(1, result.size());
        verify(scheduleRepository).findByDoctor(doctor);
    }

    @Test
    void updateSchedule_successful() {
        // Arrange
        Doctor doctor = new Doctor();
        doctor.setDoctorId(1L);

        Schedule existingSchedule = new Schedule();
        existingSchedule.setDoctor(doctor);

        Schedule updatedSchedule = new Schedule();
        updatedSchedule.setStartTime(LocalTime.of(9, 0));
        updatedSchedule.setEndTime(LocalTime.of(10, 0));

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(existingSchedule));
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(updatedSchedule);

        // Act
        scheduleService.updateSchedule(1L, 1L, updatedSchedule);

        // Assert
        verify(scheduleRepository).save(any(Schedule.class));
        verify(eventProducer).publishScheduleEvent(any(ScheduleEventDTO.class));
    }

    @Test
    void deleteSchedule_successful() {
        // Arrange
        Doctor doctor = new Doctor();
        doctor.setDoctorId(1L);

        Schedule schedule = new Schedule();
        schedule.setDoctor(doctor);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));

        // Act
        scheduleService.deleteSchedule(1L, 1L);

        // Assert
        verify(scheduleRepository).delete(schedule);
    }
}