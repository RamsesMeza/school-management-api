package com.school.management.api.auth.service;

import com.school.management.api.auth.entity.ActivationToken;
import com.school.management.api.auth.repository.ActivationTokeRepository;
import com.school.management.api.email.EmailService;
import com.school.management.api.shared.security.SecureTokenGenerator;
import com.school.management.api.shared.security.TokenHasher;
import com.school.management.api.user.User;
import com.school.management.api.user.UserRepository;
import java.time.Duration;
import java.time.Instant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserVerificationTokenService {

    private final SecureTokenGenerator secureTokenGenerator;
    private final TokenHasher tokenHasher;
    private final ActivationTokeRepository activationTokeRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public void sendVerificationEmail(Long userId) {

        User user = userRepository.getReferenceById(userId);

        String token = secureTokenGenerator.generate();
        String tokenHashed = tokenHasher.hash(token);

        ActivationToken entity = ActivationToken.builder()
                .token(tokenHashed)
                .expiresAt(Instant.now().plus(Duration.ofHours(24)))
                .userId(user)
                .build();

        activationTokeRepository.save(entity);

        emailService.sendVerificationUser(user, tokenHashed);
    }

    public void useToken(String token) {
        String tokenHashed = tokenHasher.hash(token);

        // TODO: Change to a better exeption
        ActivationToken activation = activationTokeRepository
                .findByToken(tokenHashed)
                .orElseThrow(() -> new IllegalArgumentException("No found"));

        if (activation.isExpired()) {
            throw new IllegalArgumentException("Expired");
        }

        if (activation.isRevoked()) {
            throw new IllegalArgumentException("Revoked");
        }

        activation.markAsUsed();

        activationTokeRepository.save(activation);
    }
}
