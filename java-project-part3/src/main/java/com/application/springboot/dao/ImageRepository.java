package com.application.springboot.dao;

import com.application.springboot.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Integer> {

  //Optional<User> findByEmail(String email);
}
