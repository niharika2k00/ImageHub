package com.application.springboot.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
  @Bean
  //https://kafka.apache.org/11/javadoc/org/apache/kafka/clients/producer/ProducerConfig.html
  public ProducerFactory<String, String> producerFactory() {
    Map<String, Object> props = new HashMap<>();

    // kafka broker address
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093");
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

    return new DefaultKafkaProducerFactory<>(props);
  }

  @Bean
  public KafkaTemplate<String, String> kafkaTemplate() {
    // simplify for sending messages to kafka
    return new KafkaTemplate<>(producerFactory());
  }
}
