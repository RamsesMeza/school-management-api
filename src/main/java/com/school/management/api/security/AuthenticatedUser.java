package com.school.management.api.security;

import com.school.management.api.user.Role;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticatedUser {

    private Long id;
    private String email;
    private Set<Role> roles;
}
