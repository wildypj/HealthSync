package com.wildy.doctor.kaf;

import com.wildy.doctor.schedule.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleEvent {
    private Long doctorId;
    private Schedule schedule;
}
