package com.school.management.api.user;

import com.school.management.api.user.dto.CreateUserRequest;
import com.school.management.api.user.dto.PatchUserRequest;
import com.school.management.api.user.dto.UpdateUserRequest;
import com.school.management.api.user.exception.EmailDuplicatedException;
import com.school.management.api.user.exception.UserNotFoundException;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public User create(CreateUserRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        boolean emailExist = userRepository.existsByEmail(email);

        if (emailExist) {
            throw new EmailDuplicatedException(email);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .name(request.getName())
                .lastName(request.getLastName())
                .email(email)
                .password(encodedPassword)
                .role(request.getRole())
                .status(false)
                .build();

        return userRepository.save(user);
    }

    public User update(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        user.setName(request.getName());
        user.setLastName(request.getLastName());
        user.setRole(request.getRole());

        return userRepository.save(user);
    }

    public User patch(Long id, PatchUserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        if (request.getName() != null) {
            user.setName(request.getName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        return userRepository.save(user);
    }

    public User delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        userRepository.delete(user);

        return user;
    }
}
