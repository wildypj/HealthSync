package com.wildy.doctor.kaf;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.kafka")
public class KafkaProperties {
    private String topicSchedule;
}


// to test later
// how to code a topic for kafka to create a new topic
//public class KafkaTopicConfig {
//    return TopicBuilder
//            .name("order-topic")
//            .build();
//}