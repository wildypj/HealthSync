package com.wildy.appointment.appointment.kaf;

import com.wildy.appointment.appointment.payload.AppointmentEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentEventProducer {

    // create kafka template
    private final KafkaTemplate<String, AppointmentEventDTO> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

    // Publish the event (notifications for appointments)
    public void publishAppointmentEvent(AppointmentEventDTO event) {
        kafkaTemplate.send(kafkaProperties.getTopicAppointment(), event);
    }
}
