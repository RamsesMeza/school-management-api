package com.school.management.api.auth.service;

import com.school.management.api.auth.entity.RecoverPasswordToken;
import com.school.management.api.auth.entity.User;
import com.school.management.api.auth.exception.RecoverPasswordTokenNotFound;
import com.school.management.api.auth.repository.RecoverPasswordTokenRepository;
import com.school.management.api.shared.security.SecureTokenGenerator;
import com.school.management.api.shared.security.TokenHasher;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecoverPasswordTokenService {

    @Value("${app.recover.password.token.duration}")
    private Long tokenDuration;

    private final RecoverPasswordTokenRepository recoverPasswordTokenRepository;
    private final TokenHasher tokenHasher;
    private final SecureTokenGenerator secureTokenGenerator;

    public String create(User user) {

        String token = secureTokenGenerator.generate();
        String tokenHashed = tokenHasher.hash(token);

        RecoverPasswordToken entity = RecoverPasswordToken.builder()
                .expiresAt(Instant.now().plus(Duration.ofHours(tokenDuration)))
                .token(tokenHashed)
                .user(user)
                .build();

        recoverPasswordTokenRepository.save(entity);

        return token;
    }

    public void revokePevTokens(User user) {
        List<RecoverPasswordToken> tokens =
                recoverPasswordTokenRepository.findAllByUserAndRevokedAtIsNullAndUsedAtIsNullAndExpiresAtAfter(
                        user, Instant.now());

        tokens.stream().forEach((t) -> t.revoke());

        recoverPasswordTokenRepository.saveAll(tokens);
    }

    public RecoverPasswordToken findByToken(String token) {

        String tokenHashed = tokenHasher.hash(token);

        return recoverPasswordTokenRepository
                .findByToken(tokenHashed)
                .orElseThrow(() -> new RecoverPasswordTokenNotFound());
    }

    public void useToken(RecoverPasswordToken token) {
        token.markAsUsed();
        recoverPasswordTokenRepository.save(token);
    }
}
