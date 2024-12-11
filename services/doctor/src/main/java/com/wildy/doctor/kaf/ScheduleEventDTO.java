package com.wildy.doctor.kaf;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ScheduleEventDTO {
    private Long doctorId;
    private List<ScheduleDTO> schedule;
}
