package com.metrics.dockercompose.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.Yaml;

import com.metrics.dockercompose.models.CollectorRequest;
import com.metrics.dockercompose.models.CollectorResponse;
import com.metrics.dockercompose.models.DbImages;
import com.metrics.dockercompose.models.Measurement;
import com.metrics.dockercompose.models.Metric;
import com.metrics.dockercompose.services.GitHubFileService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/collector")
public class CollectController {

  @Autowired
  private GitHubFileService gitHubFileService;

  @PostMapping("/services")
  public ResponseEntity<Object> postCollectServices(@RequestBody CollectorRequest body) {
    Map<String, Object> dockerComposeData = new HashMap<>();
    List<String> servicesWithBuild = new ArrayList<>();
    Yaml yaml = new Yaml();

    try {

      String fileContent = gitHubFileService.getFileContent(body.getDockerComposePath()).block();

      if (fileContent != null) {
        dockerComposeData = yaml.load(fileContent);
      } else {
        throw new RuntimeException("File content is null");
      }

      System.out.println(dockerComposeData);

      if (dockerComposeData == null || dockerComposeData.isEmpty()) {
        throw new RuntimeException("File content is empty");
      }

      if (!dockerComposeData.containsKey("services")) {
        throw new RuntimeException("No services found in the file");
      }

      Map<String, Object> services = (Map<String, Object>) dockerComposeData.get("services");
      
      services.forEach((serviceName, config) -> {
        Map<String, String> serviceConfig = (Map<String, String>) config;

        if (serviceConfig.containsKey("build")) {
          servicesWithBuild.add(serviceName);
        }
      });
      
      int numServices = servicesWithBuild.size();

      Measurement measurement = new Measurement(
          "Docker compose service",
          numServices,
          "absolute",
          Instant.now());

      Metric metric = new Metric(
          "Number of services",
          "static");

      CollectorResponse response = new CollectorResponse(metric, measurement);

      return ResponseEntity.status(HttpStatus.OK).body(response);

    } catch (Exception e) {
      Map<String, String> errorResponse = new HashMap<>();

      errorResponse.put("error", "Failed to read file: " + e.getMessage());

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
  }

  @PostMapping("/databases")
  public ResponseEntity<Object> postCollectDatabases(@RequestBody CollectorRequest body) {
    AtomicReference<Map<String, Object>> dockerComposeData = new AtomicReference<>(new HashMap<>());
    List<String> databases = new ArrayList<>();

    gitHubFileService.getFileContent(body.getDockerComposePath())
        .subscribe(fileContent -> {
          Yaml yaml = new Yaml();

          dockerComposeData.set(yaml.load(fileContent));
        }, error -> {
          System.err.println("Error: " + error.getMessage());
        });

    dockerComposeData.get().forEach((serviceName, config) -> {
      Map<String, String> serviceConfig = (Map<String, String>) config;

      if (serviceConfig.containsKey("image")) {
        String image = serviceConfig.get("image");

        boolean isDatabase = DbImages.DATABASE_IMAGES
            .stream()
            .anyMatch(db -> image != null && image.contains(db));

        if (isDatabase)
          databases.add(serviceName);
      }
    });

    int numDatabases = databases.size();

    Measurement measurement = new Measurement(
        "Docker compose service",
        numDatabases,
        "absolute",
        Instant.now());

    Metric metric = new Metric(
        "Number of services",
        "static");

    CollectorResponse response = new CollectorResponse(metric, measurement);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
