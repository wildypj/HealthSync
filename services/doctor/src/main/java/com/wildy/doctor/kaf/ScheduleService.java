package com.wildy.doctor.kaf;

import com.wildy.doctor.schedule.Schedule;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleEventProducer eventProducer;

    public void createSchedule(Long doctorId, Schedule schedule) {

        // Construct the ScheduleEventDTO and publish it
        ScheduleEventDTO eventDTO = new ScheduleEventDTO();
        eventDTO.setDoctorId(doctorId);

        // Convert Schedule to ScheduleDTO (assuming conversion logic here)
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setDate(schedule.getDate());
        scheduleDTO.setStartTime(schedule.getStartTime());
        scheduleDTO.setEndTime(schedule.getEndTime());
        scheduleDTO.setAvailable(schedule.isAvailable());

        eventDTO.setSchedule(List.of(scheduleDTO));

        eventProducer.publishScheduleEvent(eventDTO);
    }
}

// Publish the event
//        ScheduleEvent event = new ScheduleEvent(doctorId, schedule);
