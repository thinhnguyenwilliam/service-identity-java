package com.example.dev.controller;


import com.example.dev.dto.request.UserCreationRequest;
import com.example.dev.dto.request.UserUpdateRequest;
import com.example.dev.dto.response.ApiResponse;
import com.example.dev.dto.response.UserResponse;
import com.example.dev.entity.User;
import com.example.dev.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController
{
    UserService userService;


    @PostMapping
    ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreationRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();

        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Authenticated user: {}", authentication.getName());
        authentication.getAuthorities()
                .forEach(authority -> log.info("Role is: {}", authority.getAuthority()));

        List<UserResponse> users = userService.getUsers();

        return ApiResponse.<List<UserResponse>>builder()
                .code(1000)
                .message("Successfully retrieved users.")
                .result(users)
                .build();
    }

    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }


    @GetMapping("/{userId}")
    UserResponse getUserById(@PathVariable("userId") String id) {
        return userService.getUser(id);
    }

    @PutMapping("/{userId}")
    UserResponse updateUser(@PathVariable("userId") String id, @RequestBody UserUpdateRequest request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable("userId") String id) {
        userService.deleteUser(id);
    }
}
