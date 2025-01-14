package com.application.springboot.service;

import com.application.sharedlibrary.entity.Image;
import com.application.sharedlibrary.service.ImageService;
import com.application.springboot.dto.MediaUploadRequestDto;
import com.application.springboot.entity.User;
import com.application.springboot.utility.AuthenticatedUserLogger;
import org.json.simple.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
public class MediaService {

  private final AuthenticatedUserLogger authenticatedUserLogger;
  private final UserService userService;
  private final KafkaProducerService kafkaProducerService;
  private final ImageService imageService;

  @Autowired
  public MediaService(KafkaProducerService kafkaProducerService, AuthenticatedUserLogger authenticatedUserLogger, UserService userService, ImageService imageService) {
    this.imageService = imageService;
    this.kafkaProducerService = kafkaProducerService;
    this.authenticatedUserLogger = authenticatedUserLogger;
    this.userService = userService;
  }

  public void save(MediaUploadRequestDto reqBody, String originalImagePath, byte[] originalImgByteArr) throws Exception {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy hh:mm:ss a");
    String formattedDateTime = now.format(formatter);

    String email = authenticatedUserLogger.getLoggedInUsername();
    User userDetails = userService.findByEmail(email);

    Image image = new Image();
    image.setAuthorId(userDetails.getId());
    image.setUploadedAt(formattedDateTime);

    BeanUtils.copyProperties(reqBody, image); // (source, target)
    imageService.saveOrUpdate(image);

    // convert byte array to Base64 encoded string
    String base64EncodedString = Base64.getEncoder().encodeToString(originalImgByteArr);
    //String base64EncodedString = (String) jsonObj.get("encodedString");
    //byte[] originalImgByteArr = Base64.getDecoder().decode(base64EncodedString);

    System.out.println(image);
    JSONObject jsonObj = new JSONObject();
    jsonObj.put("id", image.getId());
    jsonObj.put("authenticatedUserEmail", email);
    jsonObj.put("originalImagePath", originalImagePath);
    jsonObj.put("message", "Image with id " + image.getId() + " is published to kafka for further processing");
    //jsonObj.put("encodedString", base64EncodedString);

    kafkaProducerService.sendMsgToTopic(jsonObj.toJSONString()); // convert JSONObject to JSON string
  }
}

//String str = "Niharika";
//byte[] byteArr = str.getBytes();
//byte[] byteArr = {78, 105, 104, 97, 114, 105, 107, 97};
//String str = new String(byteArr);
//System.out.println(str);
//System.out.println( Arrays.toString(byteArr));
