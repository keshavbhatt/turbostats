package com.ktechpit.turbostats.ingestion.controller;

import com.ktechpit.turbostats.common.model.Event;
import com.ktechpit.turbostats.ingestion.constants.RoutesConstants;
import com.ktechpit.turbostats.ingestion.service.EventIngestionService;
import com.ktechpit.turbostats.ingestion.utilities.ResponseEntityProvider;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RoutesConstants.CAPTURE_ROUTE)
@Slf4j
public class CaptureController {

  private final EventIngestionService eventIngestionService;

  @Autowired
  public CaptureController(EventIngestionService eventIngestionService) {
    this.eventIngestionService = eventIngestionService;
  }

  @PostMapping
  public ResponseEntity<Map<String, String>> captureEvent(@RequestBody Event event) {

    if (event.isInvalid()) {
      log.warn("Event is invalid");
      return ResponseEntityProvider.badRequest(event.whyInvalid());
    }

    var ingested = eventIngestionService.ingest(event);

    return ingested
        ? ResponseEntityProvider.success("Event sent")
        : ResponseEntityProvider.badRequest("Event not sent");
  }
}