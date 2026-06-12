package com.school.management.api.auth;

import com.school.management.api.auth.dto.AuthResponse;
import com.school.management.api.auth.dto.LoginRequest;
import com.school.management.api.auth.dto.RegisterRequest;
import com.school.management.api.auth.email.verification.EmailVerificationTokenService;
import com.school.management.api.auth.exception.BadCredentialsException;
import com.school.management.api.security.JwtService;
import com.school.management.api.user.Role;
import com.school.management.api.user.User;
import com.school.management.api.user.UserCreationService;
import com.school.management.api.user.UserMapper;
import com.school.management.api.user.UserRepository;
import com.school.management.api.user.dto.UserResponse;
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
    private final EmailVerificationTokenService emailVerificationTokenService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            UserMapper userMapper,
            UserCreationService userCreationService,
            EmailVerificationTokenService emailVerificationTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
        this.userCreationService = userCreationService;
        this.emailVerificationTokenService = emailVerificationTokenService;
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

        UserResponse userResponse = userCreationService.createUser(request, Set.of(Role.STUDENT), false);

        String token = emailVerificationTokenService.generateRawToken();

        System.out.println("ACTIVATION_TOKEN: " + token);
        // enviar correo

        return userResponse;
    }
}
