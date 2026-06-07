package com.school.management.api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank(message = "Name is required") private String name;

    @NotBlank(message = "Last name is required") private String lastName;

    @NotBlank(message = "Email is required") @Email(message = "Email must be valid") private String email;

    @NotBlank(message = "Password is required") @Size(min = 8, message = "Password must have at least 8 characters") private String password;
}
