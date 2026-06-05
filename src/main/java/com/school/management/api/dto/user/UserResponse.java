package com.school.management.api.dto.user;

import java.util.List;

import com.school.management.api.model.Role;
import com.school.management.api.model.User;

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
public class UserResponse {
  private Long id;
  private String name;
  private String lastName;
  private String email;
  private boolean status;
  private Role role;

  public static UserResponse toUserResponse(User user) {
    return new UserResponse(user.getId(), user.getName(), user.getLastName(),
        user.getEmail(), user.isStatus(), user.getRole());
  }

  public static List<UserResponse> toUserResponseList(List<User> users) {
    return users.stream().map(u -> toUserResponse(u)).toList();
  }

}
