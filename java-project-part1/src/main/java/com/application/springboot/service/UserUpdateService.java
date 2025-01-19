package com.application.springboot.service;

import com.application.sharedlibrary.entity.User;
import com.application.springboot.dto.PasswordUpdateRequestDto;
import com.application.springboot.dto.UserUpdateRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface UserUpdateService {
  User saveOrUpdate(User user);

  void updateUser(UserUpdateRequestDto requestBody) throws Exception;

  String updatePassword(PasswordUpdateRequestDto requestBody) throws Exception;
}
