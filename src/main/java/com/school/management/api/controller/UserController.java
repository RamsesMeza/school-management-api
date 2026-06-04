package com.school.management.api.controller;

import org.springframework.web.bind.annotation.RestController;

import com.school.management.api.dto.user.CreateUserRequest;
import com.school.management.api.dto.user.UpdateUserRequest;
import com.school.management.api.model.User;
import com.school.management.api.service.UserService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping()
  public List<User> getMethodName() {
    try {
      return userService.findAll();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ArrayList<>();
    }
  }

  @GetMapping("/{id}")
  public User getUserById(@PathVariable Long id) {
    return userService.findById(id);
  }

  @PostMapping()
  public User postMethodName(@RequestBody CreateUserRequest request) {
    return userService.create(request);
  }

  @PutMapping("{id}")
  public User putMethodName(@PathVariable Long id,
      @RequestBody UpdateUserRequest request) {

    return userService.update(id, request);
  }

}
