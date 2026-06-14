package com.school.management.api.auth.service;

import com.school.management.api.auth.dto.ICreateUserRequest;
import com.school.management.api.auth.dto.UserResponse;
import com.school.management.api.auth.entity.User;
import com.school.management.api.auth.entity.enums.Role;
import com.school.management.api.auth.entity.enums.UserStatus;
import com.school.management.api.auth.exception.UserEmailDuplicatedException;
import com.school.management.api.auth.mapper.UserMapper;
import com.school.management.api.auth.repository.UserRepository;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserCreationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserCreationService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public boolean userExistByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserResponse createUser(ICreateUserRequest request, Set<Role> roles, UserStatus status) {

        String email = request.getEmail().trim().toLowerCase();

        boolean emailExist = userRepository.existsByEmail(email);

        if (emailExist) {
            throw new UserEmailDuplicatedException(email);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .name(request.getName())
                .lastName(request.getLastName())
                .email(email)
                .password(encodedPassword)
                .roles(roles)
                .status(status)
                .build();

        return userMapper.toUserResponse(userRepository.save(user));
    }
}
