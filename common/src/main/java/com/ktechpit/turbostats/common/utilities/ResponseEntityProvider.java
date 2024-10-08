package com.ktechpit.turbostats.common.utilities;

import java.util.Map;
import org.springframework.http.ResponseEntity;

public class ResponseEntityProvider {

  public static final String MESSAGE = "message";

  private ResponseEntityProvider() {
    throw new IllegalStateException("Utility class");
  }

  public static ResponseEntity<Map<String, String>> ok(String message) {
    return ResponseEntity.ok(Map.of(MESSAGE, message));
  }

  public static ResponseEntity<Map<String, String>> badRequest(String message) {
    return ResponseEntity.badRequest().body(Map.of(MESSAGE, message));
  }
}
