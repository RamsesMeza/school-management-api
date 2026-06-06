package com.school.management.api.auth.dto;

import com.school.management.api.user.Role;
import com.school.management.api.user.dto.CreateUserRequest;

public class RegisterRequest extends CreateUserRequest {
    public RegisterRequest(String name, String lastName, String email, String password, Role role) {
        super(name, lastName, email, password, role);
    }
}
