package com.school.management.api.controller;

import org.springframework.web.bind.annotation.RestController;

import com.school.management.api.dto.user.CreateUserRequest;
import com.school.management.api.dto.user.PatchUserRequest;
import com.school.management.api.dto.user.UpdateUserRequest;
import com.school.management.api.dto.user.UserResponse;
import com.school.management.api.service.UserService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
  public ResponseEntity<List<UserResponse>> getAllUsers() {
    return ResponseEntity
        .ok(UserResponse.toUserResponseList(userService.findAll()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
    return ResponseEntity
        .ok(UserResponse.toUserResponse(userService.findById(id)));

  }

  @PostMapping()
  public ResponseEntity<UserResponse> createUser(
      @RequestBody CreateUserRequest request) {
    return ResponseEntity.status(201)
        .body(UserResponse.toUserResponse(userService.create(request)));
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
      @RequestBody UpdateUserRequest request) {
    return ResponseEntity
        .ok(UserResponse.toUserResponse(userService.update(id, request)));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<UserResponse> patchUser(@PathVariable Long id,
      @RequestBody PatchUserRequest request) {

    return ResponseEntity
        .ok(UserResponse.toUserResponse(userService.patch(id, request)));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<UserResponse> deleteUser(@PathVariable Long id) {
    return ResponseEntity
        .ok(UserResponse.toUserResponse(userService.delete(id)));
  }

}
