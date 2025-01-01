package com.application.springboot.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "image_variant")
public class ImageVariant {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private int id;

  @OneToOne
  @JoinColumn(name = "original_image_id", referencedColumnName = "id", nullable = false)
  private Image image;

  @Column(name = "img_128", nullable = false)
  @Lob
  private byte[] img128;

  @Column(name = "img_512", nullable = false)
  @Lob
  private byte[] img512;

  @Column(name = "img_1024", nullable = false)
  @Lob
  private byte[] img1024;

  @CreationTimestamp
  @Column(name = "uploaded_at", updatable = false, unique = true, length = 100)
  private final String uploadedAt;

  public ImageVariant(byte[] img128, byte[] img512, byte[] img1024, String uploadedAt) {
    this.image = image;
    this.img128 = img128;
    this.img512 = img512;
    this.img1024 = img1024;
    this.uploadedAt = uploadedAt;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Image getImage() {
    return image;
  }

  public void setImage(Image image) {
    this.image = image;
  }

  public byte[] getImg128() {
    return img128;
  }

  public void setImg128(byte[] img128) {
    this.img128 = img128;
  }

  public byte[] getImg512() {
    return img512;
  }

  public void setImg512(byte[] img512) {
    this.img512 = img512;
  }

  public byte[] getImg1024() {
    return img1024;
  }

  public void setImg1024(byte[] img1024) {
    this.img1024 = img1024;
  }

  public String getUploadedAt() {
    return uploadedAt;
  }
}
