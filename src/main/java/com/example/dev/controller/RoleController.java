package com.example.dev.controller;

import com.example.dev.dto.request.RoleRequest;
import com.example.dev.dto.response.ApiResponse;
import com.example.dev.dto.response.RoleResponse;
import com.example.dev.service.RoleService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
  RoleService roleService;

  @PostMapping
  public ApiResponse<RoleResponse> createRole(@Valid @RequestBody RoleRequest request) {
    ApiResponse<RoleResponse> apiResponse = new ApiResponse<>();
    apiResponse.setResult(roleService.createRole(request));
    apiResponse.setMessage("Role created successfully");
    return apiResponse;
  }

  @GetMapping
  public ApiResponse<List<RoleResponse>> getRoles() {
    return ApiResponse.<List<RoleResponse>>builder()
        .result(roleService.getAllRoles())
        .code(1000)
        .build();
  }

  @DeleteMapping("/{id}")
  public ApiResponse<Void> deleteRole(@PathVariable("id") String id) {
    roleService.deleteRole(id);
    return ApiResponse.<Void>builder().message("Role deleted successfully").build();
  }
}
