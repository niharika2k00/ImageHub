package com.application.springboot.service;

import com.application.sharedlibrary.dao.UserRepository;
import com.application.sharedlibrary.entity.User;
import com.application.sharedlibrary.service.UserServiceImpl;
import com.application.springboot.dto.PasswordUpdateRequestDto;
import com.application.springboot.dto.UserUpdateRequestDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserUpdateServiceImpl implements UserUpdateService {

  private final UserRepository userRepository;
  private final UserServiceImpl userServiceImpl;

  @Autowired
  public UserUpdateServiceImpl(UserRepository user_repository, UserServiceImpl user_service_impl) {
    this.userRepository = user_repository;
    this.userServiceImpl = user_service_impl;
  }

  @Override
  @Transactional
  public User saveOrUpdate(User user) {
    return userRepository.save(user);
  }

  @Override
  @Transactional
  public void updateUser(UserUpdateRequestDto requestBody) throws Exception {
    User existingUser = userServiceImpl.findById(requestBody.getId());

    BeanUtils.copyProperties(requestBody, existingUser);
    saveOrUpdate(existingUser);
  }

  @Override
  @Transactional
  public String updatePassword(PasswordUpdateRequestDto requestBody) throws Exception {
    User user = userServiceImpl.findById(requestBody.getId());
    String oldPassword = user.getPassword(); // hashed
    String newPassword = requestBody.getPassword(); // plaintext
    String message;

    if (BCrypt.checkpw(newPassword, oldPassword)) {
      message = "New password must be different from the current password.";
    } else {
      String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
      user.setPassword(hashedPassword);
      saveOrUpdate(user);
      message = "Password updated successfully";
    }

    return message;
  }
}
