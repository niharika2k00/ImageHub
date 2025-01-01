package com.application.springboot.exception;

import com.application.springboot.dto.ApiErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.naming.AuthenticationException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = CustomResourceNotFoundException.class)
  @ResponseStatus(code = HttpStatus.NOT_FOUND)
  @ResponseBody
  // Annotation response status and body required when return type is not ResponseEntity<>
  public ApiErrorResponseDto handleResourceNotFoundException(CustomResourceNotFoundException ex) {
    ApiErrorResponseDto errorResponse = new ApiErrorResponseDto(HttpStatus.NOT_FOUND.value(), ex.getMessage()); // 404
    return errorResponse;
  }

  @ExceptionHandler(value = InvalidRequestException.class)
  public ResponseEntity<ApiErrorResponseDto> handleInvalidRequestException(InvalidRequestException ex) {
    ApiErrorResponseDto errorResponse = new ApiErrorResponseDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage()); // 400
    return new ResponseEntity<ApiErrorResponseDto>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
    return new ResponseEntity<String>("Illegal argument: " + ex.getMessage(), HttpStatus.BAD_REQUEST); // 400
  }

  @ExceptionHandler(value = DatabaseAccessException.class)
  public ResponseEntity<ApiErrorResponseDto> handleDatabaseAccessException(DatabaseAccessException ex) {
    ApiErrorResponseDto body = new ApiErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()); // 500
    return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(value = ResourceAlreadyExistsException.class)
  public ResponseEntity<ApiErrorResponseDto> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
    ApiErrorResponseDto body = new ApiErrorResponseDto(HttpStatus.CONFLICT.value(), ex.getMessage()); // 409
    return new ResponseEntity<>(body, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGlobalException(Exception ex) throws Exception {
    if (ex instanceof AccessDeniedException || ex instanceof AuthenticationException) {
      throw ex;
    }
    return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
