package com.school.management.api.auth.service;

import com.school.management.api.auth.entity.RefreshToken;
import com.school.management.api.auth.entity.User;
import com.school.management.api.auth.exception.RefreshTokenException;
import com.school.management.api.auth.repository.RefreshTokenRepository;
import com.school.management.api.shared.security.SecureTokenGenerator;
import com.school.management.api.shared.security.TokenHasher;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final TokenHasher tokenHasher;
  private final SecureTokenGenerator secureTokenGenerator;
  private final RefreshTokenRepository refreshTokenRepository;

  public String create(User user) {

    String token = secureTokenGenerator.generate();
    String tokenHashed = tokenHasher.hash(token);

    RefreshToken entity = RefreshToken.builder()
        .expiresAt(Instant.now().plus(Duration.ofHours(24)))
        .token(tokenHashed)
        .user(user)
        .build();

    refreshTokenRepository.save(entity);
    return token;
  }

  public RefreshToken findByToken(String token) {

    String tokenHashed = tokenHasher.hash(token);

    RefreshToken entity = refreshTokenRepository
        .findByToken(tokenHashed)
        .orElseThrow(() -> new RefreshTokenException("Invalid token"));

    return entity;
  }

  public void revokeAllLast(User user) {
    List<RefreshToken> tokens = refreshTokenRepository.findAllByUserAndRevokedAtIsNullAndUsedAtIsNullAndExpiresAtAfter(
        user, Instant.now());

    tokens.stream().forEach((t) -> t.revoke());

    refreshTokenRepository.saveAll(tokens);
  }

  public RefreshToken revoke(String token) {

    String tokenHashed = tokenHasher.hash(token);

    RefreshToken entity = refreshTokenRepository
        .findByToken(tokenHashed)
        .orElseThrow(() -> new RefreshTokenException("Invalid token"));

    entity.revoke();

    return refreshTokenRepository.save(entity);
  }

  public RefreshToken use(String token) {

    String tokenHashed = tokenHasher.hash(token);

    RefreshToken entity = refreshTokenRepository
        .findByToken(tokenHashed)
        .orElseThrow(() -> new RefreshTokenException("Invalid token"));

    entity.markAsUsed();

    return refreshTokenRepository.save(entity);
  }

  public RefreshToken use(RefreshToken token) {
    token.markAsUsed();
    return refreshTokenRepository.save(token);
  }
}
