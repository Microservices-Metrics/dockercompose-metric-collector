package com.metrics.dockercompose.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/collector")
public class CollectController {

  private final ResourceLoader resourceLoader;

  public CollectController(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  @PostMapping()
  public void postCollect() throws IOException {
    // Identificar se tem algum container no docker-compose.yml
    // 1) identificar o serviço dentro do repo (container que está usando build a
    // partir de um diretório local)
    // 2) identificar bancos de dados (a partir de enums?)
    
    // Para facilitar a construção vou ler a partir de um arquivo local
    // TODO: trocar para ler a partir de um link de repo e verificar se tem algum arquivo chamado docker-compose.yml
    Resource resource = resourceLoader.getResource("file:docker-compose-mock.yaml");
    Yaml yaml = new Yaml();

    try (InputStream inputStream = resource.getInputStream()) {
      String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
      
      Map<String, Object> dockerComposeData = yaml.load(content);

      Map<String, Object> services = (Map<String, Object>) dockerComposeData.get("services");
      services.forEach((serviceName, config) -> {
        Map<String, String> serviceConfig = (Map<String, String>) config;
        System.out.println("Service Name: " + serviceName);
        System.out.println("Build: " + serviceConfig.get("build"));
      });
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
