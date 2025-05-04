package com.metrics.dockercompose.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.metrics.dockercompose.application.DatabaseCollectorHandler;
import com.metrics.dockercompose.application.ServiceCollectorHandler;
import com.metrics.dockercompose.models.CollectorRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/collector")
public class CollectController {

  @Autowired
  private ServiceCollectorHandler serviceCollectorHandler;

  @Autowired
  private DatabaseCollectorHandler databaseCollectorHandler;

  @PostMapping("/services")
  public ResponseEntity<Object> postCollectServices(@RequestBody CollectorRequest body) {
    Object response = serviceCollectorHandler.handleRequest(body);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping("/databases")
  public ResponseEntity<Object> postCollectDatabases(@RequestBody CollectorRequest body) {
    Object response = databaseCollectorHandler.handleRequest(body);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
