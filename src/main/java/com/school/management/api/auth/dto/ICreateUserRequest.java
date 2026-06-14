package com.school.management.api.auth.dto;

import java.time.Instant;

public interface ICreateUserRequest {
    String getName();

    String getLastName();

    String getEmail();

    String getPassword();

    Instant getEmailVerifiedAt();
}
