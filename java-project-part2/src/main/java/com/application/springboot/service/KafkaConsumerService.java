package com.application.springboot.service;

import com.application.sharedlibrary.entity.ImageVariant;
import com.application.sharedlibrary.entity.ImageVariantId;
import com.application.sharedlibrary.service.ImageVariantService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

  @Value("${custom.env.imageResolutionCount}")
  private int imageResolutionCount;

  @Value("${GROUP_ID}")
  private String kafkaConsumerGroupId;

  @Value("${WIDTH}")
  private int targetWidth;

  @Value("${HEIGHT}")
  private int targetHeight;

  public void helper(String filePath, int imageId) throws Exception {
    try {
      // process the original image to generate images in various resolution
      String resizedImagePath = imageResizeService.generateResizedImage(filePath, targetWidth, targetHeight);
      String resolution = targetWidth + "x" + targetHeight;
      System.out.println("Resized " + resolution + " image saved in path: " + resizedImagePath);

      ImageVariantId variantId = new ImageVariantId(targetWidth, targetHeight, imageId);
      ImageVariant variant = new ImageVariant();

      variant.setId(variantId);
      variant.setWidth(targetWidth);
      variant.setHeight(targetHeight);
      variant.setFilePath(resizedImagePath);

      imageVariantService.saveOrUpdate(variant);
      System.out.println("Resized image now saved successfully.");
    } catch (Exception e) {
      System.out.println("Error in processing image: " + e.getMessage());
      throw new RuntimeException(e);
    }
  }

  // Here a single Kafka topic is configured with 3 consumer groups, each containing multiple(x) consumers. Each group is responsible for handling a specific image resolution for resizing.

  // Listener with 2 consumer that handle 128 | 512 | 1024 px image resolution
  // @ConditionalOnProperty annotation helps to enable/disable listeners based on the value of environment variable dynamically https://docs.spring.io/spring-boot/api/java/org/springframework/boot/autoconfigure/condition/ConditionalOnProperty.html#matchIfMissing--
  //@ConditionalOnProperty(name = "${GROUP_ID}", havingValue = "group1")
  @KafkaListener(topics = "testtopic", groupId = "${GROUP_ID}", concurrency = "2")
  public void listenToTopic(String payload) throws Exception {
    //String targetWidth = System.getProperty("WIDTH"); // variables from VM options
    //String targetWidth = System.getenv("WIDTH"); // env variables are operating-system-level key/value pairs set globally outside of JVM. In IntelliJ these are set in the environment variable section in edit configuration

    // parse JSON string to JSON object
    JSONParser parser = new JSONParser();
    JSONObject jsonObj = (JSONObject) parser.parse(payload);

    int imageId = ((Number) jsonObj.get("id")).intValue(); // while storing(put) int is autoboxed into an Integer. And JSONObject treats int as long so need to cast to int
    String authenticatedUserEmail = (String) jsonObj.get("authenticatedUserEmail");
    String filePath = (String) jsonObj.get("originalImagePath");
    String message = (String) jsonObj.get("message");

    System.out.println("Listening from consumer..." + message);
    System.out.println("Method listenToTopic triggered for consumer group -> " + kafkaConsumerGroupId);

    helper(filePath, imageId);

    System.out.println("QUERY RESPONSE: " + imageVariantService.getCountByImageId(imageId));
    // Check if all x resolutions are processed and stored
    if (imageVariantService.getCountByImageId(imageId) == imageResolutionCount) {
      System.out.println("SEND MAIL");

      JSONObject jsonPayload = new JSONObject();
      jsonPayload.put("receiverEmail", authenticatedUserEmail);
      jsonPayload.put("text", "We are pleased to inform you that the image you uploaded has been successfully resized and converted into all the required resolutions. The processed images are now ready for use." + "\n\n" + "Thank you for using our service!");
      jsonPayload.put("subject", "Your Image Has Been Successfully Processed!");

      // producer publishes message to a kafka topic 2 for sending emails
      System.out.println("Message published to 2nd topic");
      kafkaTemplate.send("testtopic2", jsonPayload.toJSONString());
    }
  }
}
