package com.wildy.appointment.appointment.kaf;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final ScheduleEventConsumer scheduleEventConsumer;

    public boolean isDoctorAvailable(Long doctorId, LocalDate date, LocalTime time){
        return scheduleEventConsumer.isDoctorAvailable(doctorId, date, time);
    }
}
