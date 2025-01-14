package com.application.sharedlibrary.service;

import com.application.sharedlibrary.dao.ImageRepository;
import com.application.sharedlibrary.entity.Image;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService {
  private final ImageRepository imageRepository;

  @Autowired
  public ImageService(ImageRepository imageRepository) {
    this.imageRepository = imageRepository;
  }

  @Transactional
  public Image saveOrUpdate(Image image) {
    return imageRepository.save(image);
  }
}
