package com.wildy.doctor.schedule;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface ScheduleService {
    Schedule createSchedule(Long doctorId, Schedule newSchedule);

    List<Schedule> getDoctorScheduleByDate(Long doctorId, LocalDate date);

    List<Schedule> getAllDoctorSchedules(Long doctorId);

    void updateSchedule(Long doctorId, Long scheduleId, Schedule updatedSchedule);

    void deleteSchedule(Long doctorId, Long scheduleId);
}
