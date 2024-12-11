package com.wildy.notification.kaf;

import com.wildy.notification.NotificationService;
import com.wildy.notification.payload.AppointmentEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentEventConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "${spring.kafka.topic-appointment}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeAppointmentEvent(AppointmentEventDTO event) {
        log.info("Received Appointment Event for Doctor ID: {}, Patient ID: {}, Status: {}",
                event.getDoctorId(), event.getPatientId(), event.getStatus());

        // Trigger a notification based on the event type (e.g., email or SMS)
        notificationService.triggerNotification(event);
    }
}
