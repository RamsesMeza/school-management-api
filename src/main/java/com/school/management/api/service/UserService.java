package com.school.management.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.school.management.api.dto.user.CreateUserRequest;
import com.school.management.api.dto.user.PatchUserRequest;
import com.school.management.api.dto.user.UpdateUserRequest;
import com.school.management.api.exception.user.UserNotFoundException;
import com.school.management.api.model.User;
import com.school.management.api.repository.UserRepository;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> findAll() {
    return userRepository.findAll();
  }

  public User findById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User was not found"));
  }

  public User create(CreateUserRequest request) {
    User user = new User(null, request.getName(), request.getLastName(),
        request.getEmail(), request.getPassword(), true, request.getRole());

    return userRepository.save(user);

  }

  public User update(Long id, UpdateUserRequest request) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User was not found"));

    user.setName(request.getName());
    user.setLastName(request.getLastName());
    user.setRoles(request.getRole());

    return userRepository.save(user);
  }

  public User patch(Long id, PatchUserRequest request) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User was not found"));

    if (request.getName() != null) {
      user.setName(request.getName());
    }

    if (request.getLastName() != null) {
      user.setLastName(request.getLastName());
    }

    if (request.getRole() != null) {
      user.setRoles(request.getRole());
    }

    return userRepository.save(user);
  }

  public User delete(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User was not found"));

    userRepository.delete(user);

    return user;
  }

}
