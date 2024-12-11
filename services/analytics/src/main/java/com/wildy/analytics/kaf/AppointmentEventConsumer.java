package com.wildy.analytics.kaf;

import com.wildy.analytics.analytics.services.AnalyticsService;
import com.wildy.analytics.kaf.payload.AppointmentEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppointmentEventConsumer {

    private final AnalyticsService analyticsService;

    @KafkaListener(topics = "${spring.kafka.topic-appointment}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeAppointmentEvent(AppointmentEventDTO event) {
        log.info("Received Appointment Event for Doctor ID: {}, Patient ID: {}, Status: {}",
                event.getDoctorId(), event.getPatientId(), event.getStatus());

        analyticsService.processAppointmentEvent(event);
    }
}
