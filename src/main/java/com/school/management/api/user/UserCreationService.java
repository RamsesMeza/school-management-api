package com.school.management.api.user;

import com.school.management.api.user.dto.UserCreationRequest;
import com.school.management.api.user.dto.UserResponse;
import com.school.management.api.user.exception.EmailDuplicatedException;
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

    public UserResponse createUser(UserCreationRequest request, Set<Role> roles) {

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
