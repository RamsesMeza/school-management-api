package com.school.management.api.auth.exception;

public class UserEmailAlreadyVerified extends RuntimeException {

    public UserEmailAlreadyVerified() {
        super("Email is already verified");
    }
}
