package com.school.management.api.auth.service;

import com.school.management.api.auth.dto.AuthResponse;
import com.school.management.api.auth.dto.LoginRequest;
import com.school.management.api.auth.dto.RegisterRequest;
import com.school.management.api.auth.dto.ResendVerificationRequest;
import com.school.management.api.auth.dto.UserResponse;
import com.school.management.api.auth.dto.VerifyEmailRequest;
import com.school.management.api.auth.entity.User;
import com.school.management.api.auth.entity.enums.Role;
import com.school.management.api.auth.entity.enums.UserStatus;
import com.school.management.api.auth.exception.AccountDisabledException;
import com.school.management.api.auth.exception.AccountLockedException;
import com.school.management.api.auth.exception.BadCredentialsException;
import com.school.management.api.auth.exception.EmailNotVerifiedException;
import com.school.management.api.auth.exception.UserEmailAlreadyVerified;
import com.school.management.api.auth.exception.UserNotFoundException;
import com.school.management.api.auth.mapper.UserMapper;
import com.school.management.api.auth.repository.UserRepository;
import com.school.management.api.security.JwtService;
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
    private final EmailVerificationTokenService emailVerificationTokenService;

    public AuthResponse login(LoginRequest request) {

        String email = request.getEmail().trim().toLowerCase();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BadCredentialsException());

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!passwordMatches) {
            throw new BadCredentialsException();
        }

        if (user.isDeleted()) {
            throw new BadCredentialsException();
        }

        if (!user.isEmailVerified()) {
            throw new EmailNotVerifiedException();
        }

        if (user.getStatus() == UserStatus.DISABLED) {
            throw new AccountDisabledException();
        }

        if (user.getStatus() == UserStatus.LOCKED) {
            throw new AccountLockedException();
        }

        String token = jwtService.generateToken(user);
        UserResponse userResponse = userMapper.toUserResponse(user);

        return AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .message("Login success")
                .build();
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {
        UserResponse userResponse = userCreationService.createUser(request, Set.of(Role.STUDENT), UserStatus.ACTIVE);
        emailVerificationTokenService.sendVerificationEmail(userResponse.getId());

        return userResponse;
    }

    @Transactional
    public void verifyEmail(VerifyEmailRequest req) {
        emailVerificationTokenService.useToken(req.getToken());
    }

    @Transactional
    public void resendVerificationEmail(ResendVerificationRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

        if (user.isEmailVerified()) {
            throw new UserEmailAlreadyVerified();
        }

        emailVerificationTokenService.revokeEmailVerificationTokens(user);
        emailVerificationTokenService.sendVerificationEmail(user);
    }
}
