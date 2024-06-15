package com.ktechpit.turbostats.ingestion.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktechpit.turbostats.common.model.Event;
import com.ktechpit.turbostats.ingestion.constants.KafkaConstants;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class EventIngestionServiceTest {

  @Mock private KafkaTemplate<String, String> kafkaTemplate;

  @Mock private ObjectMapper objectMapper;

  @InjectMocks private EventIngestionService eventIngestionService;

  private Event event;

  @BeforeEach
  public void setUp() {
    event = new Event();
    event.setEventName("test_event");
    event.setDistinctId("12345");
    event.setProperties(Map.of("key", "value"));
  }

  @Test
  void testIngest_Success() throws JsonProcessingException {

    when(objectMapper.writeValueAsString(event)).thenReturn("eventJson");

    var result = eventIngestionService.ingest(event);

    assertTrue(result);
    var argumentCaptor = ArgumentCaptor.forClass(String.class);
    verify(kafkaTemplate, times(1)).send(eq(KafkaConstants.TOPIC_EVENTS), argumentCaptor.capture());
    assertEquals("eventJson", argumentCaptor.getValue());
  }

  @Test
  void testIngest_Failure() throws JsonProcessingException {

    when(objectMapper.writeValueAsString(event)).thenThrow(new JsonProcessingException("Error") {});

    var result = eventIngestionService.ingest(event);

    assertFalse(result);
    verify(kafkaTemplate, never()).send(anyString(), anyString());
  }
}
