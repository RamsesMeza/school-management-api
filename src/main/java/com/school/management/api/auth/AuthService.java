package com.school.management.api.auth;

import com.school.management.api.auth.dto.AuthResponse;
import com.school.management.api.auth.dto.LoginRequest;
import com.school.management.api.auth.dto.RegisterRequest;
import com.school.management.api.auth.exception.BadCredentialsException;
import com.school.management.api.security.JwtService;
import com.school.management.api.user.Role;
import com.school.management.api.user.User;
import com.school.management.api.user.UserCreationService;
import com.school.management.api.user.UserMapper;
import com.school.management.api.user.UserRepository;
import com.school.management.api.user.dto.UserResponse;
import com.school.management.api.user.exception.EmailDuplicatedException;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final UserCreationService userCreationService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
            UserMapper userMapper, UserCreationService userCreationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
        this.userCreationService = userCreationService;
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

        String token = jwtService.generateToken(user);
        UserResponse userResponse = userMapper.toUserResponse(user);

        return AuthResponse.builder().token(token).user(userResponse).build();
    }

    public UserResponse registerUser(RegisterRequest request) {

        Set<Role> roles = new HashSet<>();

        roles.add(Role.STUDENT);
        return userCreationService.createUser(request, roles);
    }
}
