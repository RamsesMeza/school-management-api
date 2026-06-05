package com.school.management.api.dto.user;

import com.school.management.api.annotations.ValidEnum;
import com.school.management.api.model.Role;

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
public class PatchUserRequest {
  private String name;
  private String lastName;

  @ValidEnum(enumClass = Role.class, message = "Role must be valid")
  private String role;
}
