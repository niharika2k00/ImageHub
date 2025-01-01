package com.application.springboot.dto;

public class UserUpdateRequestDto {

  private final int id;
  private final String name;
  private final String email;
  private final String age;
  private final String location;
  private final String bio;
  private final String phoneNumber;

  public UserUpdateRequestDto(int id, String name, String email, String age, String location, String bio, String phoneNumber) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.age = age;
    this.location = location;
    this.bio = bio;
    this.phoneNumber = phoneNumber;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getAge() {
    return age;
  }

  public String getLocation() {
    return location;
  }

  public String getBio() {
    return bio;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  @Override
  public String toString() {
    return "User{" + "id=" + id + ", name='" + name + '\'' + ", email='" + email + '\'' + ", age='" + age + ", location='" + location + '\'' + ", bio='" + bio + '\'' + ", phone number='" + phoneNumber + '\'' + '}';
  }
}
