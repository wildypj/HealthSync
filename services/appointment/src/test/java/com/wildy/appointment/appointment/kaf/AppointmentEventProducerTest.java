package com.wildy.appointment.appointment.kaf;

import com.wildy.appointment.appointment.payload.AppointmentEventDTO;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentEventProducerTest {

    @InjectMocks
    private AppointmentEventProducer appointmentEventProducer;

    @Mock
    private KafkaTemplate<String, AppointmentEventDTO> kafkaProducer;

    @Mock
    private KafkaProperties kafkaProperties;

    private AppointmentEventDTO appointmentEventDTO;

    @BeforeEach
    void setup() {
        appointmentEventDTO = new AppointmentEventDTO(
                1L, 2L, "Created", "Pending"
        );

        when(kafkaProperties.getTopicAppointment()).thenReturn("appointment-events-topic");
    }

    @Test
    void publishAppointmentEvent() {
        appointmentEventProducer.publishAppointmentEvent(appointmentEventDTO);

        // Verify the event was published
        verify(kafkaProducer).send("appointment-events-topic", appointmentEventDTO);
    }
}