package com.school.management.api.auth.exception;

public class BadCredentialsException extends RuntimeException {

    public BadCredentialsException() {
        super("Bad credentials");
    }
}
