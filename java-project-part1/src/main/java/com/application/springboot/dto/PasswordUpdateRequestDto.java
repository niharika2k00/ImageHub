package com.application.springboot.dto;

public class PasswordUpdateRequestDto {

  private final int id;
  private final String password;

  public PasswordUpdateRequestDto(int id, String password) {
    this.id = id;
    this.password = password;
  }

  public int getId() {
    return id;
  }

  public String getPassword() {
    return password;
  }
}
