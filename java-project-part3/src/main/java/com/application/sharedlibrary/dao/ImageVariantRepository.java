package com.application.sharedlibrary.dao;

import com.application.sharedlibrary.entity.ImageVariant;
import com.application.sharedlibrary.entity.ImageVariantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageVariantRepository extends JpaRepository<ImageVariant, ImageVariantId> {

}
