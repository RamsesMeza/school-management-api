package com.school.management.api.auth.dto;

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
public class UpdatePasswordRequest {
    @NotBlank(message = "Password is required") @Size(min = 8, message = "Password must have at least 8 characters") private String password;

    @NotBlank(message = "Token is required") private String token;
}
