package com.wildy.doctor.schedule.impl;

import com.wildy.doctor.doctor.Doctor;
import com.wildy.doctor.doctor.DoctorRepository;
import com.wildy.doctor.kaf.ScheduleDTO;
import com.wildy.doctor.kaf.ScheduleEvent;
import com.wildy.doctor.kaf.ScheduleEventDTO;
import com.wildy.doctor.kaf.ScheduleEventProducer;
import com.wildy.doctor.schedule.Schedule;
import com.wildy.doctor.schedule.ScheduleRepository;
import com.wildy.doctor.schedule.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final DoctorRepository doctorRepository;
    private final ScheduleRepository scheduleRepository;
    // Inject ScheduleEventProducer
    private final ScheduleEventProducer eventProducer;

    @Override
    public Schedule createSchedule(Long doctorId, Schedule newSchedule) {
        //check if doctor exist by id
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        // Check if the desired time slot is available
        List<Schedule> existingSchedules = scheduleRepository.findByDoctorAndDate(doctor, newSchedule.getDate());
        checkForScheduleOverlap(existingSchedules, newSchedule);

        newSchedule.setDoctor(doctor);
        Schedule savedSchedule =  scheduleRepository.save(newSchedule);

        // Create and Publish the event to Kafka
        ScheduleEventDTO eventDTO = new ScheduleEventDTO();
        eventDTO.setDoctorId(doctorId);

        // Convert Schedule to ScheduleDTO
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setDate(savedSchedule.getDate());
        scheduleDTO.setStartTime(savedSchedule.getStartTime());
        scheduleDTO.setEndTime(savedSchedule.getEndTime());
        scheduleDTO.setAvailable(savedSchedule.isAvailable());

        eventDTO.setSchedule(List.of(scheduleDTO));

        eventProducer.publishScheduleEvent(eventDTO);

        return savedSchedule;
    }


    @Override
    public List<Schedule> getDoctorScheduleByDate(Long doctorId, LocalDate date) {
        //check if doctor exist in db
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        return scheduleRepository.findByDoctorAndDate(doctor, date);
    }


    @Override
    public List<Schedule> getAllDoctorSchedules(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(()-> new IllegalArgumentException("Doctor not found"));
        return scheduleRepository.findByDoctor(doctor);
    }


    @Override
    public void updateSchedule(Long doctorId, Long scheduleId, Schedule updatedSchedule) {
        Schedule existingSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));

        //check if schedule belong to doctor
        if(!existingSchedule.getDoctor().getDoctorId().equals(doctorId)) {
            throw new IllegalArgumentException("Schedule does not belong to the doctor.");
        }

        // check for schedule overlap
        List<Schedule>  existingSchedules = scheduleRepository.findByDoctor(existingSchedule.getDoctor());
        checkForScheduleOverlap(existingSchedules, updatedSchedule);

        // update the schedule
        existingSchedule.setStartTime(updatedSchedule.getStartTime());
        existingSchedule.setEndTime(updatedSchedule.getEndTime());
        existingSchedule.setAvailable(updatedSchedule.isAvailable());
        scheduleRepository.save(existingSchedule);

        // Create and Publish the event to Kafka
        ScheduleEventDTO eventDTO = new ScheduleEventDTO();
        eventDTO.setDoctorId(doctorId);

        // Convert updated Schedule to ScheduleDTO
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setDate(existingSchedule.getDate());
        scheduleDTO.setStartTime(existingSchedule.getStartTime());
        scheduleDTO.setEndTime(existingSchedule.getEndTime());
        scheduleDTO.setAvailable(existingSchedule.isAvailable());

        eventDTO.setSchedule(List.of(scheduleDTO));

        eventProducer.publishScheduleEvent(eventDTO);
    }


    @Override
    public void deleteSchedule(Long doctorId, Long scheduleId) {
        // check if schedule exist in db
        Schedule existingSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));

        // check if schedule belongs to doctor
        if(!existingSchedule.getDoctor().getDoctorId().equals(doctorId)) {
            throw new IllegalArgumentException("Schedule does not belong to the doctor.");
        }

        scheduleRepository.delete(existingSchedule);
    }

    private void checkForScheduleOverlap(List<Schedule> existingSchedules, Schedule newSchedule) {
        for (Schedule schedule : existingSchedules) {
            if (newSchedule.getStartTime().isBefore(schedule.getEndTime()) &&
                    newSchedule.getEndTime().isAfter(schedule.getStartTime())) {
                throw new IllegalArgumentException("Schedule overlaps with an existing schedule.");
            }
        }
    }
}
