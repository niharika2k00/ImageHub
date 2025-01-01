package com.application.springboot.dto;

import java.util.Date;
import java.util.Optional;

public class UserLoginResponseDto {
  private final Optional<String> token;
  private final Optional<Date> expiresIn;
  private final String message;

  public UserLoginResponseDto(Optional<String> token, Optional<Date> expiresIn, String message) {
    this.token = token;
    this.expiresIn = expiresIn;
    this.message = message;
  }

  public Optional<String> getToken() {
    return token;
  }

  public Optional<Date> getExpiresIn() {
    return expiresIn;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return "UserLoginResponse{" + "token='" + token.orElse(null) + '\'' + ", expiresIn='" + expiresIn.orElse(null) + '\'' + ", message='" + message + '\'' + '}';
  }
}
