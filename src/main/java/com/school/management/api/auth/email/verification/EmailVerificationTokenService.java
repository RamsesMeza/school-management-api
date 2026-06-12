package com.school.management.api.auth.email.verification;

import org.springframework.stereotype.Service;

import com.school.management.api.shared.security.SecureTokenGenerator;
import com.school.management.api.shared.security.TokenHasher;

@Service
public class EmailVerificationTokenService {

    private final SecureTokenGenerator secureTokenGenerator;
    private final TokenHasher tokenHasher;

    public EmailVerificationTokenService(
            SecureTokenGenerator secureTokenGenerator,
            TokenHasher tokenHasher) {
        this.secureTokenGenerator = secureTokenGenerator;
        this.tokenHasher = tokenHasher;
    }

    public String generateRawToken() {
        return secureTokenGenerator.generate(32);
    }

    public String hash(String rawToken) {
        return tokenHasher.hash(rawToken);
    }
}