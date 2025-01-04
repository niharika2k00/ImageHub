package com.application.sharedlibrary.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "image_variant")
public class ImageVariant {
  // For this table `id` not as PK, rather has Composite Key (width, height, imageId)
  @EmbeddedId
  @Column(name = "id", nullable = false)
  private ImageVariantId id; // composite key

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "image_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
  private Image image;

  @Column(name = "width", nullable = false, insertable = false, updatable = false)
  private int width;

  @Column(name = "height", nullable = false, insertable = false, updatable = false)
  private int height;

  @Column(name = "file_path", nullable = false)
  private String filePath;

  // Default constructor
  public ImageVariant() {
  }

  public ImageVariant(ImageVariantId id, Image image, int width, int height, String filePath) {
    this.id = id;
    this.image = image;
    this.width = width;
    this.height = height;
    this.filePath = filePath;
  }

  public ImageVariantId getId() {
    return id;
  }

  public void setId(ImageVariantId id) {
    this.id = id;
  }

  public Image getImage() {
    return image;
  }

  public void setImage(Image image) {
    this.image = image;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  @Override
  public String toString() {
    return "Image{" + "id=" + id + ", width='" + width + '\'' + ", height='" + height + '\'' + ", filePath='" + filePath + '\'' + '}';
  }
}
