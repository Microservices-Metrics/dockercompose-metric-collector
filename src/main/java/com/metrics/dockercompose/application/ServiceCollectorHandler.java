package com.metrics.dockercompose.application;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import com.metrics.dockercompose.models.CollectorRequest;
import com.metrics.dockercompose.models.CollectorResponse;
import com.metrics.dockercompose.models.Measurement;
import com.metrics.dockercompose.models.Metric;
import com.metrics.dockercompose.services.GitHubFileService;

@Service
public class ServiceCollectorHandler {
  @Autowired
  private GitHubFileService gitHubFileService;

  public Object handleRequest(CollectorRequest request) {
    Map<String, Object> dockerComposeData = new HashMap<>();
    List<String> servicesWithBuild = new ArrayList<>();
    Yaml yaml = new Yaml();

    try {
      String fileContent = gitHubFileService.getFileContent(request.getDockerComposePath()).block();

      if (fileContent != null) {
        dockerComposeData = yaml.load(fileContent);
      } else {
        throw new RuntimeException("File content is null");
      }

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
      
      return response;
    } catch (Exception e) {
      Map<String, String> errorResponse = new HashMap<>();

      errorResponse.put("error", "Failed to read file: " + e.getMessage());
      
      return errorResponse;
    }
  }
}
