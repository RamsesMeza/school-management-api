package com.school.management.api.auth.dto;

import com.school.management.api.auth.entity.enums.Role;
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
public class UserResponse {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private boolean status;
    private Set<Role> roles;
}
