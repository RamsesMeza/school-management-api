package com.school.management.api.exception.user;

public class EmailDuplicatedException extends RuntimeException {

  public EmailDuplicatedException() {
    super("Email duplicated");
  }

}
