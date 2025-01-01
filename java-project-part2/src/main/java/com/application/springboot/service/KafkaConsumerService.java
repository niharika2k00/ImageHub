package com.application.springboot.service;

import com.application.sharedlibrary.entity.Image;
import com.application.sharedlibrary.entity.ImageVariant;
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
  private final KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  public KafkaConsumerService(ImageResizeService imageResizeService, ImageVariantService imageVariantService, KafkaTemplate<String, String> kafkaTemplate) {
    this.imageResizeService = imageResizeService;
    this.imageVariantService = imageVariantService;
    this.kafkaTemplate = kafkaTemplate;
  }

  public void helper(String payload, int width, int height) throws Exception {
    // parse JSON string to JSON object
    JSONParser parser = new JSONParser();
    JSONObject jsonObj = (JSONObject) parser.parse(payload);

    int id = (int) jsonObj.get("id");
    String message = (String) jsonObj.get("message");
    String filePath = (String) jsonObj.get("originalImagePath");

    System.out.println("Listening from consumer..." + message);
    // process the original image to generate images in various resolution
    String resizedImagePath = imageResizeService.generateResizedImage(filePath, width, height);
    String resolution = width + "x" + height;
    System.out.println("Resized " + resolution + " image stored in path: " + resizedImagePath);

    ImageVariant variant = new ImageVariant();
    Image image = new Image();

    // JPA does not treat it as a new entity to be persisted, rather treats it as an existing row in the Image table
    image.setId(id);

    variant.setImage(image);
    variant.setWidth(width);
    variant.setHeight(height);
    variant.setFilePath(resizedImagePath);

    imageVariantService.saveOrUpdate(variant);

    //resizedImages.forEach((resolution, savedImagePath) -> {
    //  System.out.println("Resolution: " + resolution);
    //  System.out.println("Stored image path: " + savedImagePath);
    //
    //  // send message via kafka producer 2
    //  //kafkaTemplate.send("testtopic2", resolution);
    //});
  }

  // Listener with 2 consumer that handlers 128px image resolution
  @KafkaListener(topics = "testtopic", groupId = "group1", concurrency = "2")
  public void listenToTopic1(String payload) throws Exception {
    String targetWidth = System.getProperty("WIDTH");
    String targetHeight = System.getProperty("HEIGHT");
    System.out.println("targetWidth:" + targetWidth);
    // env variables are operating-system-level key/value pairs set globally outside of JVM. In IntelliJ these are set in the environment variable section in edit configuration
    //targetWidth = System.getenv("WIDTH");

    helper(payload, Integer.parseInt(targetWidth), Integer.parseInt(targetHeight));
  }

  // Listener with 2 consumer that handlers 512px image resolution
  //@KafkaListener(topics = "testtopic", groupId = "group2", concurrency = "2")
  //public void listenToTopic2(String payload) throws Exception {
  //  int width = System.getenv(width);
  //  int targetHeight = System.getenv(height);
  //
  //  helper(payload, width, height);
  //
  //}

  //// Listener with 2 consumer that handlers 1024px image resolution
  //@KafkaListener(topics = "testtopic", groupId = "group3", concurrency = "2")
  //public void listenToTopic3(String payload) throws Exception {
  //  int width = System.getenv(width);
  //  int height = System.getenv(height);
  //
  //  helper(payload, width, height);
  //
  //}
}
