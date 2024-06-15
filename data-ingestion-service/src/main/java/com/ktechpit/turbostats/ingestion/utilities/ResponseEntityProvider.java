package com.ktechpit.turbostats.ingestion.utilities;

import java.util.Map;
import org.springframework.http.ResponseEntity;

public class ResponseEntityProvider {

  public static final String MESSAGE = "message";

  private ResponseEntityProvider() {

  }

  public static ResponseEntity<Map<String, String>> success(String message) {
    return ResponseEntity.ok(Map.of(MESSAGE, message));
  }

  public static ResponseEntity<Map<String, String>> badRequest(String message) {
    return ResponseEntity.badRequest().body(Map.of(MESSAGE, message));
  }
}
