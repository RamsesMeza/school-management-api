package com.school.management.api.exception.user;

public class EmailDuplicatedException extends RuntimeException {

  public EmailDuplicatedException(String email) {
    super("Email already exist: " + email);
  }

}
