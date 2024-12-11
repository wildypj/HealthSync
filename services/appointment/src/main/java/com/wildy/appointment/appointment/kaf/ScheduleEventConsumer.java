package com.wildy.appointment.appointment.kaf;

import com.wildy.appointment.appointment.payload.ScheduleDTO;
import com.wildy.appointment.appointment.payload.ScheduleEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class ScheduleEventConsumer {

    // Using the DTOs for schedule cache
    private final ConcurrentHashMap<Long, List<ScheduleDTO>> scheduleCache =  new ConcurrentHashMap<>();


    @KafkaListener(topics = "${spring.kafka.topic-schedule}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeScheduleEvent(ScheduleEventDTO event) {
        try {
//            log.info("Received Schedule Event: {}", event);
            log.info("Received Schedule Event: Doctor ID: {}, Schedule Size: {}", event.getDoctorId(), event.getSchedule().size());


            // Store the schedule information in the cache
            scheduleCache.put(event.getDoctorId(), event.getSchedule());
            log.info("Schedule for doctor {} cached successfully", event.getDoctorId());
        } catch(Exception e){
            log.error("Error processing Schedule Event: {}",e.getMessage());
        }
    }

    // Method to check if a doctor is available
    public boolean isDoctorAvailable(Long doctorId, LocalDate date, LocalTime time) {
        List<ScheduleDTO> schedules = scheduleCache.get(doctorId);
        if (schedules == null) return false;

        // Check if the doctor is available at the given time
        return schedules.stream()
                .anyMatch(schedule -> schedule.getDate().equals(date) &&
                        !schedule.getStartTime().isAfter(time) &&
                        !schedule.getEndTime().isBefore(time) &&
                        schedule.isAvailable());
    }
}
