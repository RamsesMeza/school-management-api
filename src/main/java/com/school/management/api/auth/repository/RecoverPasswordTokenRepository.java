package com.school.management.api.auth.repository;

import com.school.management.api.auth.entity.RecoverPasswordToken;
import com.school.management.api.auth.entity.User;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecoverPasswordTokenRepository extends JpaRepository<RecoverPasswordToken, Long> {

    List<RecoverPasswordToken> findAllByUserAndRevokedAtIsNullAndUsedAtIsNullAndExpiresAtAfter(User user, Instant now);

    Optional<RecoverPasswordToken> findByUser(User user);

    Optional<RecoverPasswordToken> findByToken(String token);
}
