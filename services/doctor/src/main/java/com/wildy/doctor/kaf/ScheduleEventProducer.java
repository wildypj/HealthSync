package com.wildy.doctor.kaf;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleEventProducer {

    private final KafkaTemplate<String, ScheduleEventDTO> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

    // publish the event
    public void publishScheduleEvent(ScheduleEventDTO event){
        kafkaTemplate.send(kafkaProperties.getTopicSchedule(), event);
    }
}
