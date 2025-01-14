package com.application.sharedlibrary.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Set;

@Getter
@Setter
@ToString
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

  public Image() {}

  public Image(String title, String description, String category, Integer authorId, String uploadedAt) {
    this.title = title;
    this.description = description;
    this.category = category;
    this.authorId = authorId;
    this.uploadedAt = uploadedAt;
  }

  public int getId() {return id;}

  public void setId(int id) {this.id = id;}
}
