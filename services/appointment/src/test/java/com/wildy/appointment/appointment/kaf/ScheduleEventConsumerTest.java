package com.wildy.appointment.appointment.kaf;

import com.wildy.appointment.appointment.payload.ScheduleDTO;
import com.wildy.appointment.appointment.payload.ScheduleEventDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ScheduleEventConsumerTest {

    @Mock
    private KafkaListenerEndpointRegistry registry;

    @InjectMocks
    private ScheduleEventConsumer scheduleEventConsumer;

    private ScheduleEventDTO scheduleEventDTO;

    @BeforeEach
    void setup() {
        scheduleEventDTO = new ScheduleEventDTO();
        scheduleEventDTO.setDoctorId(1L);

        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setDate(LocalDate.now());
        scheduleDTO.setStartTime(LocalTime.of(9, 0));
        scheduleDTO.setEndTime(LocalTime.of(10, 0));
        scheduleDTO.setAvailable(true);

        scheduleEventDTO.setSchedule(List.of(scheduleDTO));
    }

    @Test
    void consumeScheduleEvent() {
        // Simulate consuming the event
        scheduleEventConsumer.consumeScheduleEvent(scheduleEventDTO);

        // Verify the data was cached
        Assertions.assertTrue(scheduleEventConsumer.isDoctorAvailable(
                1L, LocalDate.now(), LocalTime.of(9, 30))
        );
    }

    @Test
    void isDoctorAvailable() {
        scheduleEventConsumer.consumeScheduleEvent(scheduleEventDTO);

        // Verify availability check for invalid time
        Assertions.assertFalse(scheduleEventConsumer.isDoctorAvailable(
                1L, LocalDate.now(), LocalTime.of(8, 30))
        );
    }
}