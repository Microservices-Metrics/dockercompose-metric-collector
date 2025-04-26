package com.metrics.dockercompose.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.Yaml;

import com.metrics.dockercompose.models.CollectorResponse;
import com.metrics.dockercompose.models.Measurement;
import com.metrics.dockercompose.models.Metric;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/collector")
public class CollectController {

  private final ResourceLoader resourceLoader;

  public CollectController(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  @PostMapping("/services")
  public ResponseEntity<Object> postCollectServices() throws IOException {
    // Para facilitar a construção vou ler a partir de um arquivo local num primeiro
    // momento
    // depois vou trocar para ler a partir de um link de repo e verificar se tem
    // algum arquivo chamado docker-compose.yml

    // TODO: trocar para ler a partir de um link de repo e verificar se tem algum
    // arquivo chamado docker-compose.yml
    Resource resource = resourceLoader.getResource("file:docker-compose.yaml");
    Yaml yaml = new Yaml();
    List<String> servicesWithBuild = new ArrayList<>();

    try (InputStream inputStream = resource.getInputStream()) {
      String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

      Map<String, Object> dockerComposeData = yaml.load(content);

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
        "static"
      );

      CollectorResponse response = new CollectorResponse(metric, measurement);

      return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (IOException e) {
      Map<String, String> errorResponse = new HashMap<>();

      errorResponse.put("error", "Failed to read file: " + e.getMessage());

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
  }

  @PostMapping("/databases")
  public void postCollectDatabases() {
    // Implementar lógica para coletar informações de bancos de dados
  }
}
