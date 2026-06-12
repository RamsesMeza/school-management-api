package com.school.management.api.user;

import com.school.management.api.user.dto.CreateUserRequest;
import com.school.management.api.user.dto.PatchUserRequest;
import com.school.management.api.user.dto.UpdateUserRequest;
import com.school.management.api.user.dto.UserResponse;
import com.school.management.api.user.exception.UserNotFoundException;
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
        return userCreationService.createUser(request, request.getRoles(), false);
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
