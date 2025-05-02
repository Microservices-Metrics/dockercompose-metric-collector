package com.metrics.dockercompose.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.metrics.dockercompose.services.GitHubFileService;

@Service
public class ServiceCollectorHandler {
  @Autowired
  private GitHubFileService dockerComposeScanner;

  // TODO: transferir pra cá a lógica do controller
}
