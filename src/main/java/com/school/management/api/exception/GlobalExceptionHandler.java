package com.school.management.api.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.school.management.api.exception.user.UserNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFound(
      UserNotFoundException exception, HttpServletRequest request) {

    ErrorResponse error = ErrorResponse.builder()
        .status(HttpStatus.NOT_FOUND.value())
        .error(HttpStatus.NOT_FOUND.getReasonPhrase())
        .message(exception.getMessage()).path(request.getRequestURI())
        .timestamp(LocalDateTime.now()).build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

}
