package com.school.management.api.auth.exception;

public class AccountLockedException extends RuntimeException {

    public AccountLockedException() {
        super("Your account has been looked. Contact an administrator.");
    }
}
