package com.application.sharedlibrary.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Set;

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
  private int authorId;

  @CreationTimestamp
  @Column(name = "uploaded_at", updatable = false, unique = true)
  private String uploadedAt;

  @OneToMany(mappedBy = "image", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  //@JoinColumn(name = "id", referencedColumnName = "image_id")
  private Set<ImageVariant> imageVariantList;

  public Set<ImageVariant> getImageVariantList() {
    return imageVariantList;
  }

  public void setImageVariantList(Set<ImageVariant> imageVariantList) {
    this.imageVariantList = imageVariantList;
  }

  public Image() {
  }

  public Image(String title, String description, String category, Integer authorId, String uploadedAt) {
    this.title = title;
    this.description = description;
    this.category = category;
    this.authorId = authorId;
    this.uploadedAt = uploadedAt;
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

  @Override
  public String toString() {
    return "Image{" + "id=" + id + ", title='" + title + '\'' + ", description='" + description + '\'' + ", category='" + category + '\'' + ", authorId='" + authorId + '\'' + ", uploadedAt='" + uploadedAt +
      '\'' + '}';
  }
}
