package com.school.management.api.auth.exception;

public class EmailVerificationTokenRevokedException extends RuntimeException {

    public EmailVerificationTokenRevokedException() {
        super("Email verification token has been revoked");
    }
}
