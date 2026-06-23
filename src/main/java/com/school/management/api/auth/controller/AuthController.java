package com.school.management.api.auth.controller;

import com.school.management.api.auth.dto.AuthResponse;
import com.school.management.api.auth.dto.LoginRequest;
import com.school.management.api.auth.dto.MessageResponse;
import com.school.management.api.auth.dto.RefreshTokenRequest;
import com.school.management.api.auth.dto.RefreshTokenResponse;
import com.school.management.api.auth.dto.RegisterRequest;
import com.school.management.api.auth.dto.ResendVerificationRequest;
import com.school.management.api.auth.dto.UpdatePasswordRequest;
import com.school.management.api.auth.dto.UserRecoverPasswordRequest;
import com.school.management.api.auth.dto.UserResponse;
import com.school.management.api.auth.dto.VerifyEmailRequest;
import com.school.management.api.auth.service.AuthService;
import com.school.management.api.auth.service.UserService;
import com.school.management.api.security.AuthenticatedUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MessageResponse.builder()
                        .message("Register success, verify your account ")
                        .build());
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal AuthenticatedUser currentUser) {
        return ResponseEntity.ok(userService.findById(currentUser.getId()));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<MessageResponse> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        authService.verifyEmail(request);
        return ResponseEntity.ok(MessageResponse.builder()
                .message("Your email was verified successfully")
                .build());
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<MessageResponse> resendVerificationEmail(
            @Valid @RequestBody ResendVerificationRequest request) {

        authService.resendVerificationEmail(request);
        return ResponseEntity.ok(MessageResponse.builder()
                .message("If the account exists, a recovery email has been sent.")
                .build());
    }

    @PostMapping("/recover-password")
    public ResponseEntity<MessageResponse> recoverPassword(@Valid @RequestBody UserRecoverPasswordRequest request) {

        authService.recoverPassword(request);
        return ResponseEntity.ok(MessageResponse.builder()
                .message("If the account exists, a recovery email has been sent.")
                .build());
    }

    @PostMapping("/update-password")
    public ResponseEntity<MessageResponse> updatePassword(@Valid @RequestBody UpdatePasswordRequest request) {

        authService.updatePassword(request);

        return ResponseEntity.ok(MessageResponse.builder()
                .message("Password updated successfully")
                .build());
    }
}
