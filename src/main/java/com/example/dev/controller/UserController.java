package com.example.dev.controller;

import com.example.dev.dto.request.PasswordCreationRequest;
import com.example.dev.dto.request.UserCreationRequest;
import com.example.dev.dto.request.UserUpdateRequest;
import com.example.dev.dto.response.ApiResponse;
import com.example.dev.dto.response.UserResponse;
import com.example.dev.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
  UserService userService;

  @PostMapping
  ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreationRequest request) {
    ApiResponse<UserResponse> apiResponse = new ApiResponse<>();

    apiResponse.setResult(userService.createUser(request));
    return apiResponse;
  }

  @PostMapping("/create-password")
  public ApiResponse<Void> createPassword(
          @Valid @RequestBody PasswordCreationRequest request) {
    userService.createPassword(request);
    ApiResponse<Void> apiResponse = new ApiResponse<>();
    apiResponse.setMessage("Password has been successfully created");
    return apiResponse;
  }


  @GetMapping
  public ApiResponse<List<UserResponse>> getUsers() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();

    log.info("Authenticated user: {}", authentication.getName());
    authentication
        .getAuthorities()
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
    return ApiResponse.<UserResponse>builder().result(userService.getMyInfo()).build();
  }

  @GetMapping("/{userId}")
  @Operation(summary = "Get user by ID", description = "Returns a single user")
  @ApiResponses(value = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Found the user"),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
  })
  UserResponse getUserById(@PathVariable("userId") String id) {
    return userService.getUser(id);
  }

  @PutMapping("/{userId}")
  UserResponse updateUser(
      @PathVariable("userId") String id, @RequestBody UserUpdateRequest request) {
    return userService.updateUser(id, request);
  }

  @DeleteMapping("/{userId}")
  void deleteUser(@PathVariable("userId") String id) {

    userService.deleteUser(id);
  }
}
