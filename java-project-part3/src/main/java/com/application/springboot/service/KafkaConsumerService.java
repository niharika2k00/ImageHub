package com.application.springboot.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

  // Consumer
  @KafkaListener(topics = "testtopic2", groupId = "group2")
  public void listenToTopic(String data) throws Exception {
    // accept data from PROJ2 after image resize
    System.out.println("consumer from proj1: " + data);

  }
}
