package com.school.management.api.auth.exception;

public class EmailVerificationTokenWasUsedException extends RuntimeException {

    public EmailVerificationTokenWasUsedException() {
        super("Email verification token was already used");
    }
}
