package com.wildy.analytics.kaf;

import com.wildy.analytics.analytics.services.AnalyticsService;
import com.wildy.analytics.kaf.payload.AppointmentEventDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AppointmentEventConsumerTest {
    @Mock
    private AnalyticsService analyticsService;

    @InjectMocks
    private AppointmentEventConsumer consumer;


    @Test
    void consumeAppointmentEvent() {
        AppointmentEventDTO event = new AppointmentEventDTO();
        event.setDoctorId(1L);
        event.setPatientId(2L);
        event.setStatus("CREATED");

        consumer.consumeAppointmentEvent(event);

        verify(analyticsService, times(1)).processAppointmentEvent(event);
    }
}