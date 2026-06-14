package com.school.management.api.auth.exception;

public class AccountDeletedException extends RuntimeException {

    public AccountDeletedException() {
        super("We have problems with your account, please contact an administrator");
    }
}
