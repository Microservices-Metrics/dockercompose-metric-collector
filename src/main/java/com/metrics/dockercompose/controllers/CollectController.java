package com.metrics.dockercompose.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/collect")
public class CollectController {
  
  @GetMapping()
  public void getCollect(@RequestParam String param) { 
    // Procurar por docker-compose.yml na raiz do repositório
    // Identificar se tem algum container no docker-compose.yml 
    // Olhar se:
    // 1) identificar o serviço dentro do repo (container que está usando build a partir de um diretório local)
    // 2) identificar bancos de dados (a partir de enums?)

    // Para facilitar a construção vou ler a partir de um arquivo local
    // TODO: trocar para ler a partir de um link de repo e verificar se tem algum arquivo chamado docker-compose.yml
    Yaml yaml = new Yaml();

    try (FileInputStream input = new FileInputStream("docker-compose-mock.yml")) {
      Map<String, Object> dockerComposeData = yaml.load(input);
      
      System.out.println(dockerComposeData);
    } catch (Exception e) {
      e.printStackTrace();
    }	
  }
}
