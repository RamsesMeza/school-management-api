package com.school.management.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.school.management.api.exception.user.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFound(
      UserNotFoundException e) {

    ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(),
        e.getMessage(), null);

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

}
