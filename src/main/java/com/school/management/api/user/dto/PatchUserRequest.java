package com.school.management.api.user.dto;

import com.school.management.api.user.Role;

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
  private Role role;
}
