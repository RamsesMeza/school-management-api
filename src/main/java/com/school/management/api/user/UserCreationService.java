package com.school.management.api.user;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.school.management.api.user.dto.CreateUserRequest;
import com.school.management.api.user.dto.UserResponse;
import com.school.management.api.user.exception.EmailDuplicatedException;

public class UserCreationService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  public UserCreationService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.userMapper = userMapper;
  }

  public UserResponse createUser(CreateUserRequest request, Set<Role> roles) {

    String email = request.getEmail().trim().toLowerCase();

    boolean emailExist = userRepository.existsByEmail(email);

    if (emailExist) {
      throw new EmailDuplicatedException(email);
    }

    String encodedPassword = passwordEncoder.encode(request.getPassword());

    User user = User.builder()
        .name(request.getName())
        .lastName(request.getLastName())
        .email(email)
        .password(encodedPassword)
        .roles(roles)
        .status(false)
        .build();

    return userMapper.toUserResponse(userRepository.save(user));
  }
}
