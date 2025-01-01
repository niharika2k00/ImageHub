package com.application.springboot.service;

import com.application.springboot.dto.PasswordUpdateRequestDto;
import com.application.springboot.dto.UserUpdateRequestDto;
import com.application.springboot.entity.User;
import com.application.springboot.exception.CustomResourceNotFoundException;
import com.application.springboot.exception.IllegalArgumentException;
import com.application.springboot.exception.InvalidRequestException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService extends UserDetailsService {
  User saveOrUpdate(User user);

  void updateUser(UserUpdateRequestDto requestBody) throws Exception;

  String updatePassword(PasswordUpdateRequestDto requestBody) throws Exception;

  List<User> findAll();

  User findById(int id) throws InvalidRequestException, IllegalArgumentException;

  User findByEmail(String email) throws CustomResourceNotFoundException;

  UserDetails loadUserByUsername(String email);

  void deleteById(int id) throws InvalidRequestException, IllegalArgumentException;
}
