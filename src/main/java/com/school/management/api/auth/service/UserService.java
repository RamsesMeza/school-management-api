package com.school.management.api.auth.service;

import com.school.management.api.auth.dto.CreateUserRequest;
import com.school.management.api.auth.dto.PatchUserRequest;
import com.school.management.api.auth.dto.UpdateUserRequest;
import com.school.management.api.auth.dto.UserResponse;
import com.school.management.api.auth.entity.User;
import com.school.management.api.auth.entity.enums.UserStatus;
import com.school.management.api.auth.exception.UserNotFoundException;
import com.school.management.api.auth.mapper.UserMapper;
import com.school.management.api.auth.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final UserCreationService userCreationService;

    public UserService(UserMapper userMapper, UserRepository userRepository, UserCreationService userCreationService) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.userCreationService = userCreationService;
    }

    public List<UserResponse> findAll() {
        return userMapper.toUserResponseList(userRepository.findAll());
    }

    public UserResponse findById(Long id) {
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }

    public UserResponse create(CreateUserRequest request) {
        return userCreationService.createUser(request, request.getRoles(), UserStatus.ACTIVE);
    }

    public UserResponse update(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        user.setName(request.getName());
        user.setLastName(request.getLastName());
        user.setRoles(request.getRoles());

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse patch(Long id, PatchUserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        if (request.getName() != null) {
            user.setName(request.getName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        userRepository.delete(user);

        return userMapper.toUserResponse(user);
    }
}
