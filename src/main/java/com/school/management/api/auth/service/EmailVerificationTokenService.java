package com.school.management.api.auth.service;

import com.school.management.api.auth.entity.EmailVerificationToken;
import com.school.management.api.auth.entity.User;
import com.school.management.api.auth.exception.EmailVerificationTokenExpiredException;
import com.school.management.api.auth.exception.EmailVerificationTokenNotFoundException;
import com.school.management.api.auth.exception.EmailVerificationTokenRevokedException;
import com.school.management.api.auth.exception.EmailVerificationTokenWasUsedException;
import com.school.management.api.auth.exception.UserNotFoundException;
import com.school.management.api.auth.repository.EmailVerificationTokenRepository;
import com.school.management.api.auth.repository.UserRepository;
import com.school.management.api.email.EmailAuthService;
import com.school.management.api.shared.security.SecureTokenGenerator;
import com.school.management.api.shared.security.TokenHasher;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailVerificationTokenService {

    private final SecureTokenGenerator secureTokenGenerator;
    private final TokenHasher tokenHasher;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final UserRepository userRepository;
    private final EmailAuthService emailAuthService;

    private void verificationEmailFlow(User user) {

        String token = secureTokenGenerator.generate();
        String tokenHashed = tokenHasher.hash(token);

        EmailVerificationToken entity = EmailVerificationToken.builder()
                .token(tokenHashed)
                .expiresAt(Instant.now().plus(Duration.ofHours(24)))
                .user(user)
                .build();

        emailVerificationTokenRepository.save(entity);

        emailAuthService.sendVerificationUser(user, token);
    }

    public void sendVerificationEmail(User user) {
        verificationEmailFlow(user);
    }

    public void sendVerificationEmail(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        verificationEmailFlow(user);
    }

    public void revokeEmailVerificationTokens(User user) {
        List<EmailVerificationToken> tokens = emailVerificationTokenRepository.findAllByUserId(user);

        tokens.stream().forEach((t) -> t.revoke());

        emailVerificationTokenRepository.saveAll(tokens);
    }

    public void useToken(String token) {
        String tokenHashed = tokenHasher.hash(token);

        EmailVerificationToken dbToken = emailVerificationTokenRepository
                .findByToken(tokenHashed)
                .orElseThrow(() -> new EmailVerificationTokenNotFoundException());

        if (dbToken.isUsed()) {
            throw new EmailVerificationTokenWasUsedException();
        }

        if (dbToken.isExpired()) {
            throw new EmailVerificationTokenExpiredException();
        }

        if (dbToken.isRevoked()) {
            throw new EmailVerificationTokenRevokedException();
        }

        dbToken.markAsUsed();

        emailVerificationTokenRepository.save(dbToken);

        Long userId = dbToken.getUser().getId();

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        user.verifyEmail();

        userRepository.save(user);
    }
}
