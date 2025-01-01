package com.application.springboot.service;

import com.application.springboot.dao.ImageRepository;
import com.application.springboot.dto.MediaUploadRequestDto;
import com.application.springboot.entity.Image;
import com.application.springboot.entity.User;
import com.application.springboot.utility.UserDetailsUtils;
import jakarta.transaction.Transactional;
import org.json.simple.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
public class MediaService {

  private final ImageRepository imageRepository;
  private final UserDetailsUtils userDetailsUtils;
  private final UserService userService;
  private final KafkaProducerService kafkaProducerService;

  @Autowired
  public MediaService(ImageRepository imageRepository, UserDetailsUtils userDetailsUtils, UserService userService, KafkaProducerService kafkaProducerService) {
    this.imageRepository = imageRepository;
    this.userDetailsUtils = userDetailsUtils;
    this.userService = userService;
    this.kafkaProducerService = kafkaProducerService;
  }

  @Transactional
  public Image saveOrUpdateImage(Image image) {
    return imageRepository.save(image);
  }

  public void save(MediaUploadRequestDto reqBody, String imgFilePath, byte[] originalImgByteArr) throws Exception {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy hh:mm:ss a");
    String formattedDateTime = now.format(formatter);

    String email = userDetailsUtils.getLoggedInUsername();
    User userDetails = userService.findByEmail(email);

    Image image = new Image();
    image.setAuthorId(userDetails.getId());
    image.setUploadedAt(formattedDateTime);
    image.setFilePath(imgFilePath);

    BeanUtils.copyProperties(reqBody, image); // (source, target)
    System.out.println(image);

    // convert byte array to Base64 encoded string
    String base64EncodedString = Base64.getEncoder().encodeToString(originalImgByteArr);

    JSONObject jsonObj = new JSONObject();
    jsonObj.put("message", "Image with id " + image.getId() + " is published to kafka for further processing");
    jsonObj.put("filePath", imgFilePath);
    //jsonObj.put("encodedString", base64EncodedString);

    kafkaProducerService.sendMsgToTopic(jsonObj.toJSONString()); // convert JSONObject to JSON string

    //saveOrUpdateImage(image);
  }
}

//String str = "Niharika";
//byte[] byteArr = str.getBytes();
//byte[] byteArr = {78, 105, 104, 97, 114, 105, 107, 97};
//String str = new String(byteArr);
//System.out.println(str);
//System.out.println( Arrays.toString(byteArr));
