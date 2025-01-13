package com.application.springboot.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigLogger {

  @Value("${server.port}")
  private String port;

  @PostConstruct
  public void logger() {
    System.out.println("Server is running on port: " + port);
  }
}
