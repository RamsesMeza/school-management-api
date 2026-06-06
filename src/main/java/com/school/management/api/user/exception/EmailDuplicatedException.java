package com.school.management.api.user.exception;

public class EmailDuplicatedException extends RuntimeException {

    public EmailDuplicatedException(String email) {
        super("Email already exist: " + email);
    }
}
