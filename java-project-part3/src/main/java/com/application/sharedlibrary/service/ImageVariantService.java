package com.application.sharedlibrary.service;

import com.application.sharedlibrary.dao.ImageVariantRepository;
import com.application.sharedlibrary.entity.ImageVariant;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageVariantService {
  private final ImageVariantRepository imageVariantRepository;

  @Autowired
  public ImageVariantService(ImageVariantRepository imageVariantRepository) {
    this.imageVariantRepository = imageVariantRepository;
  }

  @Transactional
  public ImageVariant saveOrUpdate(ImageVariant image) {
    return imageVariantRepository.save(image);
  }

}
