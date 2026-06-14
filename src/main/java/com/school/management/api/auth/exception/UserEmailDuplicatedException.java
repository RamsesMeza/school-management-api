package com.school.management.api.auth.exception;

public class UserEmailDuplicatedException extends RuntimeException {

    public UserEmailDuplicatedException(String email) {
        super("Email already exist: " + email);
    }
}
