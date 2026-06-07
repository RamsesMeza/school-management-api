package com.school.management.api.user;

import com.school.management.api.user.dto.UserResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public List<UserResponse> toUserResponseList(List<User> users) {
        return users.stream().map(u -> toUserResponse(u)).toList();
    }
}
