package com.application.springboot.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {
  @Bean
  //https://kafka.apache.org/11/javadoc/org/apache/kafka/clients/consumer/ConsumerConfig.html
  public Map<String, Object> consumerConfigs() {
    Map<String, Object> props = new HashMap<>();

    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093");
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    //props.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

    return props;
  }

  @Bean
  public ConsumerFactory<String, String> consumerFactory() {
    // creates a Consumer using the consumer properties
    return new DefaultKafkaConsumerFactory<>(consumerConfigs());
  }
}
