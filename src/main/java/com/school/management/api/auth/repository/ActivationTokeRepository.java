package com.school.management.api.auth.repository;

import com.school.management.api.auth.entity.ActivationToken;
import com.school.management.api.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivationTokeRepository extends JpaRepository<ActivationToken, Long> {

    Optional<ActivationToken> findByUserId(User userId);

    List<ActivationToken> findAllByUserId(User userId);

    Optional<ActivationToken> findByToken(String token);
}
