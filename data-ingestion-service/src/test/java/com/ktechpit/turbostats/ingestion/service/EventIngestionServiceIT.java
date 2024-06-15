package com.ktechpit.turbostats.ingestion.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktechpit.turbostats.common.model.Event;
import com.ktechpit.turbostats.ingestion.constants.KafkaConstants;
import java.util.Map;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@EmbeddedKafka(
    partitions = 1,
    topics = {KafkaConstants.TOPIC_EVENTS})
class EventIngestionServiceIT {
  @Autowired private EmbeddedKafkaBroker embeddedKafkaBroker;

  @Autowired private KafkaTemplate<String, String> kafkaTemplate;

  @Autowired private ObjectMapper objectMapper;

  private EventIngestionService eventIngestionService;

  @BeforeEach
  public void setUp() {
    eventIngestionService = new EventIngestionService(kafkaTemplate, objectMapper);
  }

  @Test
  void testIngest() {
    Event event = new Event();
    event.setEventName("test_event");
    event.setDistinctId("12345");
    event.setProperties(Map.of("key", "value"));

    Map<String, Object> consumerProps =
        KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafkaBroker);
    consumerProps.put("key.deserializer", StringDeserializer.class);
    consumerProps.put("value.deserializer", StringDeserializer.class);

    DefaultKafkaConsumerFactory<String, String> cf =
        new DefaultKafkaConsumerFactory<>(consumerProps);
    ContainerProperties containerProperties = new ContainerProperties(KafkaConstants.TOPIC_EVENTS);

    KafkaMessageListenerContainer<String, String> container =
        new KafkaMessageListenerContainer<>(cf, containerProperties);

    container.setupMessageListener(
        (MessageListener<String, String>)
            record -> {
              assertTrue(record.value().contains("\"eventName\":\"test_event\""));
              assertTrue(record.value().contains("\"distinctId\":\"12345\""));
              assertTrue(record.value().contains("\"properties\":{\"key\":\"value\"}"));
            });

    container.start();

    boolean result = eventIngestionService.ingest(event);

    assertTrue(result);

    container.stop();
  }
}
