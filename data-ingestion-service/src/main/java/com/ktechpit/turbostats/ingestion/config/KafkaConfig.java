package com.ktechpit.turbostats.ingestion.config;

import com.ktechpit.turbostats.ingestion.constants.KafkaConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

  @Bean
  public NewTopic topic() {
    return TopicBuilder
        .name(KafkaConstants.TOPIC_EVENTS)
        .build();
  }
}
