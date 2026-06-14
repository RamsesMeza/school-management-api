package com.school.management.api.auth.repository;

import com.school.management.api.auth.entity.EmailVerificationToken;
import com.school.management.api.auth.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

    Optional<EmailVerificationToken> findByUserId(User userId);

    List<EmailVerificationToken> findAllByUserId(User userId);

    Optional<EmailVerificationToken> findByToken(String token);
}
