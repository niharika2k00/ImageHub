package com.application.springboot.service;

import com.application.springboot.entity.Role;
import com.application.springboot.entity.User;
import com.application.springboot.exception.CustomResourceNotFoundException;
import com.application.springboot.exception.IllegalArgumentException;
import com.application.springboot.exception.InvalidRequestException;
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
