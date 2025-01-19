package com.application.springboot.service;

import com.application.sharedlibrary.entity.Role;
import com.application.sharedlibrary.entity.User;
import com.application.sharedlibrary.exception.CustomResourceNotFoundException;
import com.application.sharedlibrary.exception.InvalidRequestException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public interface RoleService {
  Role saveOrUpdate(Role role);

  List<Role> findAll();

  Role findById(int id) throws InvalidRequestException, IllegalArgumentException;

  Role findByRoleName(String roleName) throws CustomResourceNotFoundException;

  void deleteById(int id) throws InvalidRequestException, IllegalArgumentException;

  User grantRolesToUser(int userId, HashSet<String> assignedRoles) throws Exception;
}
