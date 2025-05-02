package com.metrics.dockercompose.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class GitHubFileService {
  private final WebClient webClient;

  public GitHubFileService(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.baseUrl("https://raw.githubusercontent.com").build();
  }

  public Mono<String> getFileContent(String path) {
    path = path.replace("/blob/", "/refs/heads/"); 
    
    return webClient.get()
        .uri(path)
        .retrieve()
        .bodyToMono(String.class)
        .onErrorResume(e -> Mono.just("Error fetching file: " + e.getMessage()));
  }
}
