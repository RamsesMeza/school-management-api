package com.school.management.api.shared.entity;

import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class TokenEntity extends AuditableEntity {
    private String token;
    private Instant expiresAt;
    private Instant usedAt;
    private Instant revokedAt;

    public boolean isUsed() {
        return usedAt != null;
    }

    public boolean isRevoked() {
        return revokedAt != null;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public void revoke() {
        this.revokedAt = Instant.now();
    }

    public void markAsUsed() {
        this.usedAt = Instant.now();
    }
}
