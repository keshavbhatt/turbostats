package com.ktechpit.turbostats.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
  @JsonProperty("event_name")
  private String eventName;

  @JsonProperty("distinct_id")
  private String distinctId;

  @JsonProperty("properties")
  private Map<String, Object> properties;

  @JsonIgnore
  public boolean isInvalid() {
    return eventIsNullOrEmpty() || distinctIdIsNullOrEmpty();
  }

  @JsonIgnore
  public String whyInvalid() {
    var reasons = new StringBuilder("Missing required parameter(s): ");
    if (eventIsNullOrEmpty()) {
      reasons.append("event_name ");
    }
    if (distinctIdIsNullOrEmpty()) {
      reasons.append("distinct_id ");
    }
    return reasons.toString().trim();
  }

  @JsonIgnore
  private boolean eventIsNullOrEmpty() {
    return eventName == null || eventName.isEmpty();
  }

  @JsonIgnore
  private boolean distinctIdIsNullOrEmpty() {
    return distinctId == null || distinctId.isEmpty();
  }
}
