package com.school.management.api.auth.exception;

public class AccountDisabledException extends RuntimeException {

    public AccountDisabledException() {
        super("Your account has been disabled. Contact an administrator.");
    }
}
