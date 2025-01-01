package com.application.springboot.exception;

public class IllegalArgumentException extends Exception {

  public IllegalArgumentException(String message) {
    super(message);
  }

  public IllegalArgumentException(String message, Throwable cause) {
    super(message, cause);
  }
}
