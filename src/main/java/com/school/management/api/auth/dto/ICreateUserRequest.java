package com.school.management.api.auth.dto;

public interface ICreateUserRequest {
    String getName();

    String getLastName();

    String getEmail();

    String getPassword();
}
