package com.application.springboot.service;

import com.application.sharedlibrary.entity.ImageVariant;
import com.application.sharedlibrary.entity.ImageVariantId;
import com.application.sharedlibrary.entity.User;
import com.application.sharedlibrary.service.ImageVariantService;
import com.application.sharedlibrary.service.UserService;
import com.application.sharedlibrary.util.EmailTemplateProcessor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Service
public class KafkaConsumerService {

  private final ImageResizeService imageResizeService;
  private final ImageVariantService imageVariantService;
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final UserService userService;
  private final EmailTemplateProcessor emailTemplateProcessor;

  @Autowired
  public KafkaConsumerService(ImageResizeService imageResizeService, ImageVariantService imageVariantService, KafkaTemplate<String, String> kafkaTemplate, UserService userService, EmailTemplateProcessor emailTemplateProcessor) {
    this.imageResizeService = imageResizeService;
    this.imageVariantService = imageVariantService;
    this.kafkaTemplate = kafkaTemplate;
    this.userService = userService;
    this.emailTemplateProcessor = emailTemplateProcessor;
  }

  @Value("${custom.target-image-resolution-count}")
  private int targetImageResolutionCount;

  @Value("${GROUP_ID}")
  private String kafkaConsumerGroupId;

  @Value("${WIDTH}")
  private int targetWidth;

  @Value("${HEIGHT}")
  private int targetHeight;

  // Here a single Kafka topic is configured with 3 consumer groups (passed in the ENV), each containing multiple(x) consumers. Each group is responsible for handling a specific image resolution for resizing.

  // Listener with 2 consumer that handle 128 | 512 | 1024 px image resolution
  // @ConditionalOnProperty annotation helps to enable/disable listeners based on the value of environment variable dynamically https://docs.spring.io/spring-boot/api/java/org/springframework/boot/autoconfigure/condition/ConditionalOnProperty.html
  // @ConditionalOnProperty(name = "${GROUP_ID}", havingValue = "group1")
  @KafkaListener(topics = "testtopic", groupId = "${GROUP_ID}", concurrency = "2")
  public void listenToTopic(String payload) throws Exception {
    //String targetWidth = System.getProperty("WIDTH"); // variables from VM options
    //String targetWidth = System.getenv("WIDTH"); // env variables are operating-system-level key/value pairs set globally outside of JVM. In IntelliJ these are set in the environment variable section in edit configuration

    // parse JSON string to JSON object
    JSONParser parser = new JSONParser();
    JSONObject jsonObj = (JSONObject) parser.parse(payload);

    int imageId = ((Number) jsonObj.get("id")).intValue(); // while storing(put) int is autoboxed into an Integer. And JSONObject treats int as long so need to cast to int
    int authenticatedUserId = ((Number) jsonObj.get("authenticatedUserId")).intValue();
    String filePath = (String) jsonObj.get("originalImagePath");
    String message = (String) jsonObj.get("message");

    System.out.println(message + " A consumer in the group " + kafkaConsumerGroupId + " is now listening to the topic for processing tasks.");
    helper(filePath, imageId);

    int liveCount = imageVariantService.getCountByImageId(imageId); // fetch number of rows with base id
    System.out.println(liveCount + getSuffix(liveCount) + " image successfylly generated.");

    // Check if all x resolutions are processed and stored
    if (liveCount == targetImageResolutionCount) {
      User authenticatedUser = new User();
      authenticatedUser = userService.findById(authenticatedUserId);

      Path path = Paths.get("./image_processed_email.md");
      String mailBodyMd = Files.readString(path);

      // Mapping placeholders for replacement
      Map<String, String> replacements = Map.of(
        "{{username}}", authenticatedUser.getName().toUpperCase()
      );

      String mailBodyHtml = emailTemplateProcessor.processContent(mailBodyMd, replacements);

      JSONObject jsonPayload = new JSONObject();
      jsonPayload.put("subject", "Your Image Has Been Successfully Processed!");
      jsonPayload.put("body", mailBodyHtml);
      jsonPayload.put("receiverEmail", authenticatedUser.getEmail());

      // producer publishes message to a kafka topic 2 for sending emails
      System.out.println("Message published to 2nd topic...Sending mail...");
      kafkaTemplate.send("testtopic2", jsonPayload.toJSONString());
    }
  }

  public void helper(String filePath, int imageId) throws Exception {
    try {
      // process the original image to generate images in various resolution
      String resizedImagePath = imageResizeService.generateResizedImage(filePath, targetWidth, targetHeight);
      String resolution = targetWidth + "x" + targetHeight;
      System.out.println("Image successfully resized at resolution " + resolution + " and saved in path " + resizedImagePath);

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

  private String getSuffix(int number) {
    if (number % 100 >= 11 && number % 100 <= 13) { // case for handling 11, 12, 13
      return "th";
    }
    switch (number % 10) {
      case 1:
        return "st";
      case 2:
        return "nd";
      case 3:
        return "rd";
      default:
        return "th";
    }
  }
}
