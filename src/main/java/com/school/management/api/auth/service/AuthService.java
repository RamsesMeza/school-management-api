package com.school.management.api.auth.service;

import com.school.management.api.auth.dto.AuthResponse;
import com.school.management.api.auth.dto.LoginRequest;
import com.school.management.api.auth.dto.RefreshTokenRequest;
import com.school.management.api.auth.dto.RefreshTokenResponse;
import com.school.management.api.auth.dto.RegisterRequest;
import com.school.management.api.auth.dto.ResendVerificationRequest;
import com.school.management.api.auth.dto.UpdatePasswordRequest;
import com.school.management.api.auth.dto.UserRecoverPasswordRequest;
import com.school.management.api.auth.dto.UserResponse;
import com.school.management.api.auth.dto.VerifyEmailRequest;
import com.school.management.api.auth.entity.RecoverPasswordToken;
import com.school.management.api.auth.entity.RefreshToken;
import com.school.management.api.auth.entity.User;
import com.school.management.api.auth.entity.enums.Role;
import com.school.management.api.auth.entity.enums.UserStatus;
import com.school.management.api.auth.exception.AccountDisabledException;
import com.school.management.api.auth.exception.AccountLockedException;
import com.school.management.api.auth.exception.BadCredentialsException;
import com.school.management.api.auth.exception.EmailNotVerifiedException;
import com.school.management.api.auth.exception.RecoverPasswordException;
import com.school.management.api.auth.exception.RefreshTokenException;
import com.school.management.api.auth.exception.UserEmailAlreadyVerified;
import com.school.management.api.auth.exception.UserNotFoundException;
import com.school.management.api.auth.mapper.UserMapper;
import com.school.management.api.auth.repository.UserRepository;
import com.school.management.api.email.EmailAuthService;
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
    private final EmailAuthService emailAuthService;
    private final RecoverPasswordTokenService recoverPasswordTokenService;
    private final RefreshTokenService refreshTokenService;

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
        String refreshToken = refreshTokenService.create(user);
        UserResponse userResponse = userMapper.toUserResponse(user);

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
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

    @Transactional
    public void recoverPassword(UserRecoverPasswordRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new RecoverPasswordException("If the account exists, a recovery email has been sent."));

        recoverPasswordTokenService.revokePevTokens(user);
        String token = recoverPasswordTokenService.create(user);

        emailAuthService.sendRecoverPasswordEmail(user, token);
    }

    @Transactional
    public void updatePassword(UpdatePasswordRequest request) {

        RecoverPasswordToken token = recoverPasswordTokenService.findByToken(request.getToken());

        boolean invalidToken = token.isExpired() || token.isRevoked() || token.isUsed();

        if (invalidToken) {
            throw new RecoverPasswordException("Invalid token");
        }

        Long userId = token.getUser().getId();

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        String newPasswordEncoded = passwordEncoder.encode(request.getPassword());
        user.setPassword(newPasswordEncoded);

        userRepository.save(user);
        recoverPasswordTokenService.useToken(token);
    }

    @Transactional
    public RefreshTokenResponse refresh(RefreshTokenRequest request) {

        RefreshToken token = refreshTokenService.findByToken(request.getRefreshToken());

        boolean invalidToken = token.isExpired() || token.isRevoked() || token.isUsed();

        if (invalidToken) {
            throw new RefreshTokenException("Invalid token");
        }

        refreshTokenService.use(token);

        User user = token.getUser();

        String newToken = jwtService.generateToken(user);
        String refreshToken = refreshTokenService.create(user);

        return RefreshTokenResponse.builder().token(newToken).refreshToken(refreshToken).build();

    }
}
