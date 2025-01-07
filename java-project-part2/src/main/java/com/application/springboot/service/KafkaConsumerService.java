package com.application.springboot.service;

import com.application.sharedlibrary.entity.ImageVariant;
import com.application.sharedlibrary.entity.ImageVariantId;
import com.application.sharedlibrary.service.ImageVariantService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

  private final ImageResizeService imageResizeService;
  private final ImageVariantService imageVariantService;
  private final EmailSenderService emailSenderService;
  private final KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  public KafkaConsumerService(ImageResizeService imageResizeService, ImageVariantService imageVariantService, EmailSenderService emailSenderService, KafkaTemplate<String, String> kafkaTemplate) {
    this.imageResizeService = imageResizeService;
    this.imageVariantService = imageVariantService;
    this.emailSenderService = emailSenderService;
    this.kafkaTemplate = kafkaTemplate;
  }

  public void helper(String payload, int width, int height) throws Exception {
    // parse JSON string to JSON object
    JSONParser parser = new JSONParser();
    JSONObject jsonObj = (JSONObject) parser.parse(payload);

    int id = ((Number) jsonObj.get("id")).intValue(); // while storing(put) int is autoboxed into an Integer. And JSONObject treats int as long so need to cast to int
    String message = (String) jsonObj.get("message");
    String filePath = (String) jsonObj.get("originalImagePath");

    System.out.println("Listening from consumer..." + message);
    // process the original image to generate images in various resolution
    String resizedImagePath = imageResizeService.generateResizedImage(filePath, width, height);
    String resolution = width + "x" + height;
    System.out.println("Resized " + resolution + " image stored in path: " + resizedImagePath);

    ImageVariantId variantId = new ImageVariantId(width, height, id);
    ImageVariant variant = new ImageVariant();

    variant.setId(variantId);
    variant.setWidth(width);
    variant.setHeight(height);
    variant.setFilePath(resizedImagePath);

    imageVariantService.saveOrUpdate(variant);
    System.out.println("Resized image now inserted successfully.");

    System.out.println("QUERY RESPONSE: " + imageVariantService.getCountByImageId(id));
    if (imageVariantService.getCountByImageId(id) == 3) {
      System.out.println("SEND MAIL");
      emailSenderService.sendEmail();
    }
  }

  String kafkaConsumerGroupId = System.getProperty("GROUP_ID");

  // Here a single Kafka topic is configured with 3 consumer groups, each containing multiple(x) consumers. Each group is responsible for handling a specific image resolution for resizing.

  // Listener with 2 consumer that handle 128 | 512 | 1024 px image resolution
  // @ConditionalOnProperty annotation helps to enable/disable listeners based on the value of environment variable dynamically https://docs.spring.io/spring-boot/api/java/org/springframework/boot/autoconfigure/condition/ConditionalOnProperty.html#matchIfMissing--
  //@ConditionalOnProperty(name = "${GROUP_ID}", havingValue = "group1")
  @KafkaListener(topics = "testtopic", groupId = "${GROUP_ID}", concurrency = "2")
  public void listenToTopic(String payload) throws Exception {
    String targetWidth = System.getProperty("WIDTH");
    String targetHeight = System.getProperty("HEIGHT");
    System.out.println("Method listenToTopic triggered for consumer group -> " + kafkaConsumerGroupId);
    // env variables are operating-system-level key/value pairs set globally outside of JVM. In IntelliJ these are set in the environment variable section in edit configuration
    //targetWidth = System.getenv("WIDTH");

    helper(payload, Integer.parseInt(targetWidth), Integer.parseInt(targetHeight));
  }
}
