package com.application.sharedlibrary.dao;

import com.application.sharedlibrary.entity.ImageVariant;
import com.application.sharedlibrary.entity.ImageVariantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageVariantRepository extends JpaRepository<ImageVariant, ImageVariantId> {
  // Both valid | done in 2 ways

  //@Query(value = "SELECT COUNT(*) FROM image_variant WHERE image_id = :id", nativeQuery = true)
  //int getCountByImageId(@Param("id") int imageId);

  @Query("SELECT COUNT(*) FROM ImageVariant i WHERE i.image.id = ?1")
  int getCountByImageId(int imageId);
}
