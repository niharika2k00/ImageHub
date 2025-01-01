package com.application.springboot.exception;

public class ResourceAlreadyExistsException extends Exception {

  public ResourceAlreadyExistsException(String message) {
    super(message);
  }

  public ResourceAlreadyExistsException(String message, Throwable cause) {
    super(message, cause);
  }
}
