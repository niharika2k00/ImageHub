package com.application.springboot.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

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
  // to hide this field from response
  private String password;

  @Column(name = "age")
  private String age;

  @Column(name = "location")
  private String location;

  @Column(name = "bio")
  private String bio;

  @Column(name = "phone_number")
  // <- column name, but in req body pass phoneNumber
  private String phoneNumber;

  // join table that maps users to their respective roles (as it's a many-to-many relationship)
  // Hence, need to create a separate table to hold the foreign keys
  // https://www.javaguides.net/2023/07/jpa-jointable-annotation.html
  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  // Entity Class - hence no argument constructor
  public User() {
  }

  public User(String name, String email, String password, String age, String location, String bio, String phoneNumber) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.age = age;
    this.bio = bio;
    this.location = location;
    this.phoneNumber = phoneNumber;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getAge() {
    return age;
  }

  public void setAge(String age) {
    this.age = age;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

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
