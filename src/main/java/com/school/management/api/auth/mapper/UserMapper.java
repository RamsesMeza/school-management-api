package com.school.management.api.auth.mapper;

import com.school.management.api.auth.dto.UserResponse;
import com.school.management.api.auth.entity.User;
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
                .roles(user.getRoles())
                .status(user.getStatus())
                .isDeleted(user.isDeleted())
                .isEmailVerified(user.isEmailVerified())
                .build();
    }

    public List<UserResponse> toUserResponseList(List<User> users) {
        return users.stream().map(u -> toUserResponse(u)).toList();
    }
}
