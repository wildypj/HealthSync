package com.wildy.doctor.kaf;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"doctor-schedule-topic"}, bootstrapServersProperty = "spring.kafka.producer.bootstrap-servers")
class ScheduleEventProducerTest {

    //Start the Kafka container
    @Autowired
    private KafkaProperties kafkaProperties;
    @Autowired
    private KafkaTemplate<String, ScheduleEventDTO> kafkaTemplate;

    private EmbeddedKafkaBroker embeddedKafkaBroker;

    private Map<String, Object> consumerProps;

    @BeforeEach
    void setUp(@Autowired EmbeddedKafkaBroker broker) {
        this.embeddedKafkaBroker = broker;
        this.consumerProps = KafkaTestUtils.consumerProps("test-group", "true", embeddedKafkaBroker);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
    }

    @Test
    void publishScheduleEvent() {
        //Create Consumer
        var consumerFactory = new DefaultKafkaConsumerFactory<String, ScheduleEventDTO>(consumerProps,
                new StringDeserializer(), new JsonDeserializer<>(ScheduleEventDTO.class, false));

        var consumer = consumerFactory.createConsumer();
        embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer);

        // Prepare test data
        ScheduleEventDTO event = new ScheduleEventDTO();
        event.setDoctorId(1L);

        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setDate(LocalDate.now());
        scheduleDTO.setStartTime(LocalTime.of(9, 0));
        scheduleDTO.setEndTime(LocalTime.of(10, 0));
        scheduleDTO.setAvailable(true);
        event.setSchedule(List.of(scheduleDTO));

        // Send the event
        kafkaTemplate.send(kafkaProperties.getTopicSchedule(), event);

        // Poll for the event
        ConsumerRecord<String, ScheduleEventDTO> record = KafkaTestUtils.getSingleRecord(consumer, kafkaProperties.getTopicSchedule());

        // Verify the event
        assertThat(record).isNotNull();
        assertThat(record.value().getDoctorId()).isEqualTo(1L);
        assertThat(record.value().getSchedule().get(0).getDate()).isEqualTo(scheduleDTO.getDate());
    }
}