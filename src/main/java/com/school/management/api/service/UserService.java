package com.school.management.api.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.school.management.api.model.Roles;
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

  public void save() {
    userRepository.save(new User(null, "Ramses", "Meza",
        "ram@gmail.com", "1234", true, Roles.ADMIN));
  }

}
