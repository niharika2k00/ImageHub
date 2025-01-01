package com.application.springboot.service;

import com.application.springboot.dao.RoleRepository;
import com.application.springboot.entity.Role;
import com.application.springboot.entity.User;
import com.application.springboot.exception.CustomResourceNotFoundException;
import com.application.springboot.exception.IllegalArgumentException;
import com.application.springboot.exception.InvalidRequestException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleServiceImplementation implements RoleService {

  private final RoleRepository roleRepository;
  private final UserService userService;

  // use method-injection
  //@Autowired
  //public void setUserService(UserService userservice) {
  //  this.userService = userservice;
  //}

  @Autowired
  public RoleServiceImplementation(RoleRepository role_repository, UserService user_service) {
    this.roleRepository = role_repository;
    this.userService = user_service;
  }

  @Override
  @Transactional
  public Role saveOrUpdate(Role role) {
    return roleRepository.save(role);
  }

  @Override
  public List<Role> findAll() {
    System.out.println("Total roles in DB: " + roleRepository.count());
    return roleRepository.findAll();
  }

  @Override
  public Role findById(int id) throws InvalidRequestException, IllegalArgumentException {
    if (id < 0) throw new IllegalArgumentException("Invalid ID. ID must be a non-negative number.");
    Role roleData;
    //roleData = roleRepository.findById(id).get();
    Optional<Role> optionalRole = roleRepository.findById(id);

    if (optionalRole.isPresent()) {
      roleData = optionalRole.get();
    } else {
      throw new InvalidRequestException("Role with the specified ID (" + id + ") was not found. Please verify the ID and try again.");
    }

    return roleData;
  }

  @Override
  public Role findByRoleName(String roleName) throws CustomResourceNotFoundException {
    Role roleData;
    Optional<Role> optionalRole = roleRepository.findByRoleName(roleName);

    if (optionalRole.isPresent()) {
      roleData = optionalRole.get();
    } else {
      throw new CustomResourceNotFoundException("Provided role name does not exist.");
    }

    return roleData;
  }

  @Override
  @Transactional
  public void deleteById(int id) throws InvalidRequestException, IllegalArgumentException {
    findById(id);
    roleRepository.deleteById(id);
  }

  // array of assigned roles for a specific user
  public User grantRolesToUser(int userId, HashSet<String> assignedRoleList) throws Exception {
    User user = userService.findById(userId);
    Set<Role> roleSet = new HashSet<>();

    // fetch existing user roles and append
    Set<Role> existingRoleList = user.getRoles();
    roleSet.addAll(existingRoleList);

    for (String roleName : assignedRoleList) {
      Role role = findByRoleName(roleName);
      roleSet.add(role);
    }

    for (Role role : roleSet) {
      System.out.println(role);
    }

    user.setRoles(roleSet);
    return userService.saveOrUpdate(user);
  }
}
