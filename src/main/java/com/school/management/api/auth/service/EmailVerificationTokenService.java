package com.school.management.api.auth.service;

import com.school.management.api.auth.entity.EmailVerificationToken;
import com.school.management.api.auth.entity.User;
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

    public void sendVerificationEmail(Long userId) {

        User user = userRepository.getReferenceById(userId);

        String token = secureTokenGenerator.generate();
        String tokenHashed = tokenHasher.hash(token);

        EmailVerificationToken entity = EmailVerificationToken.builder()
                .token(tokenHashed)
                .expiresAt(Instant.now().plus(Duration.ofHours(24)))
                .userId(user)
                .build();

        emailVerificationTokenRepository.save(entity);

        emailAuthService.sendVerificationUser(user, tokenHashed);
    }

    public void revokeEmailVerificationTokens(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<EmailVerificationToken> tokens = emailVerificationTokenRepository.findAllByUserId(user);

        tokens.stream().forEach((t) -> t.revoke());

        emailVerificationTokenRepository.saveAll(tokens);
        System.out.println(tokens);
    }

    public void useToken(String token) {
        String tokenHashed = tokenHasher.hash(token);

        // TODO: Change to a better exeption
        EmailVerificationToken activation = emailVerificationTokenRepository
                .findByToken(tokenHashed)
                .orElseThrow(() -> new IllegalArgumentException("No found"));

        if (activation.isExpired()) {
            throw new IllegalArgumentException("Expired");
        }

        if (activation.isRevoked()) {
            throw new IllegalArgumentException("Revoked");
        }

        activation.markAsUsed();

        emailVerificationTokenRepository.save(activation);
    }
}
