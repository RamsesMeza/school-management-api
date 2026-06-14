package com.school.management.api.auth.exception;

public class EmailVerificationTokenNotFoundException extends RuntimeException {

    public EmailVerificationTokenNotFoundException() {
        super("Email verification token was not found");
    }
}
