package com.application.springboot.dto;

import org.springframework.web.multipart.MultipartFile;

public class MediaUploadRequestDto {

  String title;
  String description;
  String category;
  MultipartFile imageFile;

  public MediaUploadRequestDto(String title, String description, String category, MultipartFile imageFile) {
    this.title = title;
    this.description = description;
    this.category = category;
    this.imageFile = imageFile;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public MultipartFile getImageFile() {
    return imageFile;
  }

  public void setImageFile(MultipartFile imageFile) {
    this.imageFile = imageFile;
  }
}
