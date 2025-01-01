package com.application.springboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomResourceNotFoundException extends Exception {

  public CustomResourceNotFoundException(String message) {
    super(message);
  }

  public CustomResourceNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
