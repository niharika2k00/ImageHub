package com.application.sharedlibrary.service;

import com.application.sharedlibrary.dao.UserRepository;
import com.application.sharedlibrary.entity.Role;
import com.application.sharedlibrary.entity.User;
import com.application.sharedlibrary.exception.CustomResourceNotFoundException;
import com.application.sharedlibrary.exception.InvalidRequestException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

  private final UserRepository userRepository;

  @Autowired
  public UserServiceImpl(UserRepository user_repository) {
    this.userRepository = user_repository;
  }

  @Override
  public List<User> findAll() {
    System.out.println("Total users in DB: " + userRepository.count());
    return userRepository.findAll();
  }

  @Override
  public User findById(int id) throws InvalidRequestException, IllegalArgumentException {
    if (id < 0)
      throw new IllegalArgumentException("Invalid ID. ID must be a non-negative number.");
    User userData;
    //userData = userRepository.findById(id).get();
    Optional<User> optionalUser = userRepository.findById(id);

    if (optionalUser.isPresent()) {
      userData = optionalUser.get();
    } else {
      throw new InvalidRequestException("User with the specified ID (" + id + ") was not found. Please verify the ID and try again.");
    }

    return userData;
  }

  @Override
  public User findByEmail(String email) throws CustomResourceNotFoundException {
    User userData;
    Optional<User> optionalUser = userRepository.findByEmail(email);

    if (optionalUser.isPresent()) { // user found
      userData = optionalUser.get();
    } else {
      throw new CustomResourceNotFoundException("User with the provided email does not exist.");
    }

    return userData;
  }

  @Override
  public UserDetails loadUserByUsername(String email) {
    User userData = null;
    try {
      userData = findByEmail(email);
    } catch (CustomResourceNotFoundException e) {
      throw new RuntimeException(e);
    }

    List<String> roleList = new ArrayList<>();
    Set<Role> roles = userData.getRoles();

    for (Role role : roles) {
      String roleName = role.getRoleName();
      roleList.add(roleName.substring(5));
    }

    return org.springframework.security.core.userdetails.User.builder()
      .username(userData.getEmail())
      .password(userData.getPassword())
      .roles(roleList.toArray(new String[0]))
      .build();
  }

  @Override
  @Transactional
  public void deleteById(int id) throws InvalidRequestException, IllegalArgumentException {
    findById(id);
    userRepository.deleteById(id);
  }
}
