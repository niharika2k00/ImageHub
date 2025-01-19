package com.application.sharedlibrary.service;

import com.application.sharedlibrary.entity.User;
import com.application.sharedlibrary.exception.CustomResourceNotFoundException;
import com.application.sharedlibrary.exception.InvalidRequestException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService extends UserDetailsService {

  List<User> findAll();

  User findById(int id) throws InvalidRequestException, IllegalArgumentException;

  User findByEmail(String email) throws CustomResourceNotFoundException;

  UserDetails loadUserByUsername(String email);

  void deleteById(int id) throws InvalidRequestException, IllegalArgumentException;
}
