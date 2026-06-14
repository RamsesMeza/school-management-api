package com.school.management.api.auth.exception;

public class EmailVerificationTokenExpiredException extends RuntimeException {

    public EmailVerificationTokenExpiredException() {
        super("Email verification token has been expired");
    }
}
