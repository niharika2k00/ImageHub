package com.application.springboot.controller;

import com.application.springboot.dto.MediaUploadRequestDto;
import com.application.springboot.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class MediaController {

  private final MediaService mediaService;

  @Autowired
  public MediaController(MediaService mediaService) {
    this.mediaService = mediaService;
  }

  @Value("${custom.path.source-image-dir}")
  String sourceImageDirectory;

  @PostMapping("/upload/image")
  public String uploadMedia(@ModelAttribute MediaUploadRequestDto reqBody) throws Exception {
    // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/multipart/MultipartFile.html
    // retrieve data from the DTO
    MultipartFile imageFile = reqBody.getImageFile();
    String imageFilePath = "";

    // process the file and save to the disk and store in DB
    if (!imageFile.isEmpty()) {
      Random random = new Random();
      int uuid = 100000 + random.nextInt(900000); // 6-digit unique no
      String uploadDir = sourceImageDirectory; // relative path starts from ./javaProject directory (at src level)
      Path uploadDirPath = Paths.get(uploadDir);

      String fileName = uuid + "_" + imageFile.getOriginalFilename();
      Path filePath = Paths.get(uploadDir, fileName); // combine by joining the sub paths
      imageFilePath = filePath.toString();

      // create directory if not exists
      if (!Files.exists(uploadDirPath)) {
        Files.createDirectories(uploadDirPath);
      }

      // directory if doesn't have write access
      if (!Files.isWritable(uploadDirPath)) {
        throw new IOException("Directory is not writable: " + uploadDir);
      }

      Files.copy(imageFile.getInputStream(), filePath);
      System.out.println("filePath: " + filePath);
      System.out.println("File uploaded to: " + filePath.toAbsolutePath());
      System.out.println("File uploaded to relative path: " + imageFilePath);
      System.out.println("File size in bytes:" + imageFile.getSize());

      String contentType = imageFile.getContentType();
      System.out.println("Content type: " + contentType);
      if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
        throw new IllegalArgumentException("Only JPEG or PNG images are allowed");
      }
    }

    mediaService.save(reqBody, imageFilePath, imageFile.getBytes());

    return "Media uploaded and processing started successfully!";
  }
}
