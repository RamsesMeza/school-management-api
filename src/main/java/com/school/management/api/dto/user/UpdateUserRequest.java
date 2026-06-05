package com.school.management.api.dto.user;

import com.school.management.api.annotations.ValidEnum;
import com.school.management.api.model.Role;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {

  @NotBlank(message = "Name is required")
  private String name;

  @NotBlank(message = "Last name is required")
  private String lastName;

  @NotBlank(message = "Roles is required")
  @ValidEnum(enumClass = Role.class, message = "Role must be valid")
  private String role;
}