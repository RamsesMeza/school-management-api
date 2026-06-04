package com.school.management.api.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.school.management.api.dto.user.CreateUserRequest;
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
        .orElseThrow(() -> new NoSuchElementException("Error"));
  }

  public User create(CreateUserRequest request) {

    User user = new User(null, request.getName(), request.getLastName(),
        request.getEmail(), request.getPassword(), true, request.getRole());

    return userRepository.save(user);

  }

}
