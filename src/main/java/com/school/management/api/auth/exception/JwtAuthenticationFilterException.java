package com.school.management.api.auth.exception;

public class JwtAuthenticationFilterException extends RuntimeException {

    public JwtAuthenticationFilterException() {
        super("Bad authentication");
    }
}
