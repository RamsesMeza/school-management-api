package com.school.management.api.auth.repository;

import com.school.management.api.auth.entity.RefreshToken;
import com.school.management.api.auth.entity.User;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    List<RefreshToken> findAllByUserAndRevokedAtIsNullAndUsedAtIsNullAndExpiresAtAfter(User user, Instant now);

    Optional<RefreshToken> findByToken(String token);
}
