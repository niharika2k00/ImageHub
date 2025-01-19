package com.application.springboot.service;

import com.application.sharedlibrary.entity.Role;
import com.application.sharedlibrary.entity.User;
import com.application.sharedlibrary.exception.CustomResourceNotFoundException;
import com.application.sharedlibrary.exception.InvalidRequestException;
import com.application.sharedlibrary.service.UserService;
import com.application.springboot.dao.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

  private final UserService userService;
  private final UserUpdateService userUpdateService;
  private final RoleRepository roleRepository;

  // use method-injection
  //@Autowired
  //public void setUserService(UserService userservice) {
  //  this.userUpdateService = userservice;
  //}

  @Autowired
  public RoleServiceImpl(RoleRepository role_repository, UserService user_service, UserUpdateService user_update_service) {
    this.userService = user_service;
    this.userUpdateService = user_update_service;
    this.roleRepository = role_repository;
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
    if (id < 0)
      throw new IllegalArgumentException("Invalid ID. ID must be a non-negative number.");
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
  public void deleteById(int id) throws IllegalArgumentException, InvalidRequestException {
    findById(id);
    roleRepository.deleteById(id);
  }

  // array of assigned roles for a specific user
  @Override
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
    return userUpdateService.saveOrUpdate(user);
  }
}
