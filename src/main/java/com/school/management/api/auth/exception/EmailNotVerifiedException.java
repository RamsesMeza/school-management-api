package com.school.management.api.auth.exception;

public class EmailNotVerifiedException extends RuntimeException {

    public EmailNotVerifiedException() {
        super("You must verify your email before continue");
    }
}
