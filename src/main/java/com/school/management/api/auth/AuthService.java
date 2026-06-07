package com.school.management.api.auth;

import com.school.management.api.auth.dto.AuthResponse;
import com.school.management.api.auth.dto.LoginRequest;
import com.school.management.api.auth.dto.RegisterRequest;
import com.school.management.api.auth.exception.BadCredentialsException;
import com.school.management.api.user.Role;
import com.school.management.api.user.User;
import com.school.management.api.user.UserRepository;
import com.school.management.api.user.dto.UserResponse;
import com.school.management.api.user.exception.EmailDuplicatedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(LoginRequest request) {

        String email = request.getEmail().trim().toLowerCase();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BadCredentialsException());

        if (!user.isActive()) {
            throw new BadCredentialsException();
        }

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!passwordMatches) {
            throw new BadCredentialsException();
        }

        UserResponse userResponse = UserResponse.toUserResponse(user);

        return AuthResponse.builder().token(null).user(userResponse).build();
    }

    public User registerUser(RegisterRequest request) {

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
                .role(Role.STUDENT)
                .status(false)
                .build();

        return userRepository.save(user);
    }
}
