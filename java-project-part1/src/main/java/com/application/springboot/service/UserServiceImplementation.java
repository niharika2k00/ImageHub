package com.application.springboot.service;

import com.application.springboot.dao.UserRepository;
import com.application.springboot.dto.PasswordUpdateRequestDto;
import com.application.springboot.dto.UserUpdateRequestDto;
import com.application.springboot.entity.Role;
import com.application.springboot.entity.User;
import com.application.springboot.exception.CustomResourceNotFoundException;
import com.application.springboot.exception.IllegalArgumentException;
import com.application.springboot.exception.InvalidRequestException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImplementation implements UserService, UserDetailsService {

  private final UserRepository userRepository;

  @Autowired
  public UserServiceImplementation(UserRepository user_repository) {
    this.userRepository = user_repository;
  }

  @Override
  @Transactional
  public User saveOrUpdate(User user) {
    return userRepository.save(user);
  }

  @Override
  @Transactional
  public void updateUser(UserUpdateRequestDto requestBody) throws Exception {
    User existingUser = findById(requestBody.getId());

    BeanUtils.copyProperties(requestBody, existingUser);
    saveOrUpdate(existingUser);
  }

  @Override
  @Transactional
  public String updatePassword(PasswordUpdateRequestDto requestBody) throws Exception {
    User user = findById(requestBody.getId());
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

    if (optionalUser.isPresent()) {
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
