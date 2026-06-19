package com.school.management.api.auth.entity;

import com.school.management.api.auth.entity.enums.Role;
import com.school.management.api.auth.entity.enums.UserStatus;
import com.school.management.api.professor.ProfessorProfile;
import com.school.management.api.shared.entity.AuditableEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "professor_id")
    private ProfessorProfile professorProfile;

    @OneToMany(mappedBy = "user")
    private Set<EmailVerificationToken> emailVerificationTokens;

    @OneToMany(mappedBy = "user")
    private Set<RecoverPasswordToken> recoverPasswordTokens;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Set<Role> roles;

    private Instant emailVerifiedAt;

    private Instant deletedAt;

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public boolean isEmailVerified() {
        return emailVerifiedAt != null;
    }

    public boolean isActive() {
        return !isDeleted() && isEmailVerified() && status == UserStatus.ACTIVE;
    }

    public void verifyEmail() {
        if (this.deletedAt != null) {
            throw new IllegalStateException("Deleted user cannot verify email");
        }

        if (this.emailVerifiedAt == null) {
            this.emailVerifiedAt = Instant.now();
        }
    }

    public void disable() {
        if (this.deletedAt != null) {
            throw new IllegalStateException("Deleted user cannot be disabled");
        }

        this.status = UserStatus.DISABLED;
    }

    public void lock() {
        if (this.deletedAt != null) {
            throw new IllegalStateException("Deleted user cannot be locked");
        }

        this.status = UserStatus.LOCKED;
    }

    public String getFullName() {
        return name + " " + lastName;
    }
}
