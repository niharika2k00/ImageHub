package com.application.sharedlibrary.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private int id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "email", unique = true, nullable = false)
  private String email;

  @Column(name = "password", nullable = false)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password; // to hide this field from response

  @Column(name = "age")
  private String age;

  @Column(name = "location")
  private String location;

  @Column(name = "bio")
  private String bio;

  @Column(name = "phone_number")
  private String phoneNumber;

  // join table that maps users to their respective roles (as it's a many-to-many relationship)
  // Hence, need to create a separate table to hold the foreign keys
  // https://www.javaguides.net/2023/07/jpa-jointable-annotation.html
  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  // Entity Class - hence no argument constructor
  public User() {}

  public User(String name, String email, String password, String age, String location, String bio, String phoneNumber) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.age = age;
    this.bio = bio;
    this.location = location;
    this.phoneNumber = phoneNumber;
  }

  // Add getters and setters. Can be ignored by using Lombok annotations
  //public int getId() {return id;}
  //public void setId(int id) {this.id = id;}

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  @Override
  public String toString() {
    return "User{" + "id=" + id + ", name='" + name + '\'' + ", email='" + email + '\'' + ", age='" + age + ", password='" + password + '\'' + ", location='" + location + '\'' + ", bio='" + bio + '\'' + ", phone number='" + phoneNumber + '\'' + '}';
  }
}
