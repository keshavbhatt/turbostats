package com.ktechpit.turbostats.ingestion.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktechpit.turbostats.common.model.Event;
import com.ktechpit.turbostats.ingestion.constants.KafkaConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventIngestionService {
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  public EventIngestionService(
      KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
  }

  public boolean ingest(Event event) {
    try {
      kafkaTemplate.send(KafkaConstants.TOPIC_EVENTS, objectMapper.writeValueAsString(event));
      return true;
    } catch (JsonProcessingException e) {
      return false;
    }
  }
}
