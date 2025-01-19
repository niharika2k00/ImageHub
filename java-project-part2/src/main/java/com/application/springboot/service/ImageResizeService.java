package com.application.springboot.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageResizeService {

  // Predefined popular sizes
  //private final int[][] imageResolutions = {{128, 128}, {512, 512}, {1024, 1024}};

  /*
   Why not passing byte[] of image from controller -> kafka producer -> consumer -> service ????
   - Performance: smaller Kafka messages reduce latency and resource usage
   - Decoupling: file path ensures that the producer and consumer services are loosely coupled and do not depend on transferring heavy payloads
  */

  // byte[] imageBytes
  public String generateResizedImage(String filePath, int width, int height) throws Exception {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    String extension = filePath.contains(".") ? filePath.substring(filePath.lastIndexOf(".") + 1) : "";

    String outputDirectory = "../photoGallery";
    String outputFileName = outputDirectory + File.separator + extractBaseImageFileName(filePath) + "_resized_" + width + "x" + height + "." + extension;
    File outputFile = new File(outputFileName);
    outputFile.getParentFile().mkdirs(); // create directory if not exists

    //save resized image to file
    Thumbnails.of(filePath)
      .size(width, height)
      .outputFormat(extension)
      .outputQuality(1)
      .toFile(outputFile);

    //generate resized image byte array
    Thumbnails.of(filePath) // OR Thumbnails.of(Arrays.toString(originalImageBytes))
      .size(width, height)
      .outputFormat(extension)
      .outputQuality(1)
      .toOutputStream(outputStream);

    byte[] resizedImageBytes = outputStream.toByteArray();
    return outputFileName;
  }

  private String extractBaseImageFileName(String filePath) {
    Path path = Paths.get(filePath);
    String fileName = path.getFileName().toString();
    fileName = fileName.substring(0, fileName.lastIndexOf("."));

    return fileName;
  }
}
