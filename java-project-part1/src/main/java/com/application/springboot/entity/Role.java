package com.application.springboot.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private int id;

  @Column(name = "role_name", unique = true, nullable = false)
  private String roleName;

  // On the target side, we only have to provide the name of the field, which maps the relationship
  @ManyToMany(mappedBy = "roles")
  private Set<User> users = new HashSet<>();

  public Role() {}

  public Role(String roleName) {
    this.roleName = roleName;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  // Note: NO getter method for users as bidirectional relationship is modeled as an object reference in Java, so while retrieving data, the full object along with its
  // relationships to be serialized, resulting in a nested structure in the response.
  public void setUsers(Set<User> users) {
    this.users = users;
  }

  //public Set<User> getUsers() {
  //  return users;
  //}

  @Override
  public String toString() {
    return "Role{" + "id=" + id + ", roleName='" + roleName + '\'' + '}';
  }
}
