package com.application.sharedlibrary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

// creating composite key class for Image Variant table
@Embeddable
public class ImageVariantId implements Serializable {
  private int width;
  private int height;
  @Column(name = "image_id")
  private int imageId;

  public ImageVariantId() {}

  public ImageVariantId(int width, int height, int imageId) {
    this.width = width;
    this.height = height;
    this.imageId = imageId;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;

    ImageVariantId imageVariantId = (ImageVariantId) obj;

    return Objects.equals(width, imageVariantId.width) && Objects.equals(height, imageVariantId.height) && Objects.equals(imageId, imageVariantId.imageId);
  }

  @Override
  public int hashCode() {
    // Generate hash code from fields
    return Objects.hash(width, height, imageId);
  }

  public int getWidth() {return width;}

  public int getHeight() {return height;}

  public int getImageId() {return imageId;}
}
