package com.school.management.api.user;

import java.util.List;

import org.springframework.stereotype.Service;

import com.school.management.api.user.dto.CreateUserRequest;
import com.school.management.api.user.dto.PatchUserRequest;
import com.school.management.api.user.dto.UpdateUserRequest;
import com.school.management.api.user.exception.EmailDuplicatedException;
import com.school.management.api.user.exception.UserNotFoundException;

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
        .orElseThrow(() -> new UserNotFoundException(id));
  }

  public User create(CreateUserRequest request) {

    boolean emailExist = userRepository.existsByEmail(request.getEmail());

    if (emailExist) {
      throw new EmailDuplicatedException(request.getEmail());
    }

    User user = new User(null, request.getName().toLowerCase(),
        request.getLastName().toLowerCase(), request.getEmail().toLowerCase(),
        request.getPassword(), true, request.getRole());

    return userRepository.save(user);
  }

  public User update(Long id, UpdateUserRequest request) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));

    user.setName(request.getName());
    user.setLastName(request.getLastName());
    user.setRole(request.getRole());

    return userRepository.save(user);
  }

  public User patch(Long id, PatchUserRequest request) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));

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
    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));

    userRepository.delete(user);

    return user;
  }

}
