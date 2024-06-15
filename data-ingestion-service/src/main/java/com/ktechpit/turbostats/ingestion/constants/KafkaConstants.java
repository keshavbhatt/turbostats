package com.ktechpit.turbostats.ingestion.constants;

public class KafkaConstants {

  public static final String TOPIC_EVENTS = "events";

  private KafkaConstants() {
    throw new IllegalStateException(Constants.UTILITY_CLASS);
  }
}
