package com.school.management.api.auth.dto;

import com.school.management.api.auth.entity.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {

    @NotBlank(message = "Name is required") private String name;

    @NotBlank(message = "Last name is required") private String lastName;

    @NotNull(message = "Role is required") private Set<Role> roles;
}
