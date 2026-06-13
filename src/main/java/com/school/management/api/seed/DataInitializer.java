package com.school.management.api.seed;

import com.school.management.api.user.Role;
import com.school.management.api.user.UserCreationService;
import com.school.management.api.user.UserStatus;
import com.school.management.api.user.dto.CreateUserRequest;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Value("${app.admin.email}")
    private String email;

    @Value("${app.admin.password}")
    private String password;

    private final UserCreationService userCreationService;

    public DataInitializer(UserCreationService userCreationService) {
        this.userCreationService = userCreationService;
    }

    @Override
    public void run(String... args) {

        if (!userCreationService.userExistByEmail(email)) {
            CreateUserRequest request = CreateUserRequest.builder()
                    .email(email)
                    .password(password)
                    .name("System")
                    .lastName("Admin")
                    .build();

            userCreationService.createUser(request, Set.of(Role.ADMIN), UserStatus.DISABLED);
        }
    }
}
