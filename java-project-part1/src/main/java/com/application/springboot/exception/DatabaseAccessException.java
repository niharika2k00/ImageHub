package com.application.springboot.exception;

public class DatabaseAccessException extends Exception {

  public DatabaseAccessException(String message) {
    super(message);
  }

  public DatabaseAccessException(String message, Throwable cause) {
    super(message, cause);
  }
}
