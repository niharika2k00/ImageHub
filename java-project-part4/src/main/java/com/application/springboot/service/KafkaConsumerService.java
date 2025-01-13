package com.application.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

  private final EmailSenderService emailSenderService;

  @Autowired
  public KafkaConsumerService(EmailSenderService emailSenderService) {
    this.emailSenderService = emailSenderService;
  }

  @KafkaListener(topics = "testtopic2", groupId = "group1", concurrency = "2", topicPartitions = {@TopicPartition(topic = "testtopic2", partitions = {"0", "1"})})
  public void listenToTopic(String payload) throws Exception {
    System.out.println("Consumed from producer 2" + payload);
    emailSenderService.sendEmail();
  }
}
