package com.school.management.api.auth.exception;

public class RecoverPasswordTokenNotFound extends RuntimeException {

    public RecoverPasswordTokenNotFound() {
        super("Recover password token was not found");
    }
}
