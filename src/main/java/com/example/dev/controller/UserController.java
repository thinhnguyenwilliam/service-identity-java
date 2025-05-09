package com.example.dev.controller;


import com.example.dev.dto.request.UserCreationRequest;
import com.example.dev.dto.request.UserUpdateRequest;
import com.example.dev.entity.User;
import com.example.dev.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController
{
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    User createUser(@Valid @RequestBody UserCreationRequest request) {
        return userService.createUserRequest(request);
    }

    @GetMapping
    List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    User getUserById(@PathVariable("userId") String id) {
        return userService.getUser(id);
    }

    @PutMapping("/{userId}")
    User updateUser(@PathVariable("userId") String id, @RequestBody UserUpdateRequest request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable("userId") String id) {
        userService.deleteUser(id);
    }
}
