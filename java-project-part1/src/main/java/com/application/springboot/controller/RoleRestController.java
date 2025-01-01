package com.application.springboot.controller;

import com.application.springboot.entity.Role;
import com.application.springboot.entity.User;
import com.application.springboot.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RoleRestController {

  private final RoleService roleService;

  @Autowired
  public RoleRestController(RoleService roleservice) {
    this.roleService = roleservice;
  }

  // GET all /roles
  @GetMapping("/roles")
  public List<Role> findAll() {
    return roleService.findAll();
  }

  // GET /roles/{id}
  @GetMapping("/roles/{id}")
  public Role findById(@PathVariable int id) throws Exception {
    return roleService.findById(id);
  }

  // POST /roles - add new role
  @PostMapping("/roles")
  public Role createNewRole(@RequestBody Role role) {
    role.setId(0);
    Role newRoleObject = roleService.saveOrUpdate(role);
    System.out.println("Success! New role added. " + newRoleObject);
    return newRoleObject;
  }

  // PUT /roles - update existing role with modified object
  @PutMapping("/roles")
  public String updateRole(@RequestBody Role updatedRole) {
    roleService.saveOrUpdate(updatedRole);
    return "Role updated successfully";
  }

  // DELETE /roles/{id}
  @DeleteMapping("/roles/{id}")
  public String deleteRole(@PathVariable int id) throws Exception {
    roleService.deleteById(id);
    System.out.println("Successfully deleted role with id " + id);
    return "Successfully deleted role with id " + id;
  }

  // POST /users/{id}/roles
  @PostMapping("/users/{id}/roles")
  public User assignRolesToUser(@PathVariable int id, @RequestBody HashSet<String> assignedRoleList) throws Exception {
    System.out.println(assignedRoleList);
    return roleService.grantRolesToUser(id, assignedRoleList);
  }
}
