package com.application.springboot.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "image")
public class Image {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private int id;

  @Column(name = "title")
  private String title;

  @Column(name = "description")
  private String description;

  @Column(name = "category")
  private String category;

  @Column(name = "author_id")
  private Integer authorId;

  @CreationTimestamp
  @Column(name = "uploaded_at", updatable = false, unique = true)
  private String uploadedAt;

  @Column(name = "file_path")
  private String filePath;

  public Image() {
  }

  public Image(String title, String description, String category, Integer authorId, String uploadedAt, String filePath, String duration) {
    this.title = title;
    this.description = description;
    this.category = category;
    this.authorId = authorId;
    this.uploadedAt = uploadedAt;
    this.filePath = filePath;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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

  public Integer getAuthorId() {
    return authorId;
  }

  public void setAuthorId(Integer authorId) {
    this.authorId = authorId;
  }

  public String getUploadedAt() {
    return uploadedAt;
  }

  public void setUploadedAt(String uploadedAt) {
    this.uploadedAt = uploadedAt;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  @Override
  public String toString() {
    return "Image{" + "id=" + id + ", title='" + title + '\'' + ", description='" + description + '\'' + ", category='" + category + '\'' + ", authorId='" + authorId + '\'' + ", uploadedAt='" + uploadedAt +
      '\'' + ", filePath='" + filePath + '\'' + '}';
  }
}
