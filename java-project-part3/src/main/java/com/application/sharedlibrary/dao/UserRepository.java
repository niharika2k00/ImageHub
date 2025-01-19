package com.application.sharedlibrary.dao;

import com.application.sharedlibrary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//public interface <name> extends JpaRepository<entityType, primaryKey>
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  //  @Query(value = "select * from user where email = ?1", nativeQuery = true)
  //  @Query("select u from User u where u.email = ?1")
  Optional<User> findByEmail(String email);
}
