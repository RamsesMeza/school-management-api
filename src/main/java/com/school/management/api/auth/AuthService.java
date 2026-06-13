package com.school.management.api.auth;

import com.school.management.api.auth.dto.AuthResponse;
import com.school.management.api.auth.dto.LoginRequest;
import com.school.management.api.auth.dto.RegisterRequest;
import com.school.management.api.auth.exception.BadCredentialsException;
import com.school.management.api.auth.service.UserVerificationTokenService;
import com.school.management.api.security.JwtService;
import com.school.management.api.user.Role;
import com.school.management.api.user.User;
import com.school.management.api.user.UserCreationService;
import com.school.management.api.user.UserMapper;
import com.school.management.api.user.UserRepository;
import com.school.management.api.user.UserStatus;
import com.school.management.api.user.dto.UserResponse;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final UserCreationService userCreationService;
    private final UserVerificationTokenService userVerificationTokenService;

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

    @Transactional
    public UserResponse register(RegisterRequest request) {

        UserResponse userResponse = userCreationService.createUser(request, Set.of(Role.STUDENT), UserStatus.DISABLED);
        userVerificationTokenService.verify(userResponse.getId());

        return userResponse;
    }
}
