package com.application.springboot.utility;

import com.application.sharedlibrary.entity.Role;
import com.application.springboot.dao.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoleInitializer {

  public RoleRepository roleRepository;

  @Autowired
  public RoleInitializer(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  // Add default roles in the DB when application starts
  @EventListener(ApplicationReadyEvent.class)
  public void initializeDefaultRoles() {
    List<Role> roleList = new ArrayList<>();

    roleList.add(new Role("ROLE_ADMIN"));
    roleList.add(new Role("ROLE_DEVELOPER"));
    roleList.add(new Role("ROLE_EDITOR"));
    roleList.add(new Role("ROLE_GUEST"));
    roleList.add(new Role("ROLE_MANAGER"));
    roleList.add(new Role("ROLE_OPERATOR"));
    roleList.add(new Role("ROLE_SUPER_ADMIN"));
    roleList.add(new Role("ROLE_USER"));
    roleList.add(new Role("ROLE_VIEWER"));

    for (Role role : roleList) {
      // If role doesn't exist in DB
      if (roleRepository.findByRoleName(role.getRoleName()).isEmpty()) {
        System.out.println("Adding role " + role.getRoleName() + " in the database...");
        roleRepository.save(role);
      }
    }
  }
}
