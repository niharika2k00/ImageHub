package com.application.springboot.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  public String kafkaServerPort;

  @Bean
  public ConsumerFactory<String, String> consumerFactory() {
    // creates a Consumer using the consumer properties https://kafka.apache.org/11/javadoc/org/apache/kafka/clients/consumer/ConsumerConfig.html
    Map<String, Object> config = new HashMap<>();

    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerPort); // kafka broker address
    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    config.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, "20000000"); // 20 MB
    config.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, "20000000");
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    config.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "20000000");
    config.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "20000000");
    //config.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");

    return new DefaultKafkaConsumerFactory<>(config);
  }
}
