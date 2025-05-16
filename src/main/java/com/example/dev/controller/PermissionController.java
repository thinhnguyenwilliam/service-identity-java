package com.example.dev.controller;


import com.example.dev.dto.request.PermissionRequest;
import com.example.dev.dto.response.ApiResponse;
import com.example.dev.dto.response.PermissionResponse;
import com.example.dev.service.PermissionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    public ApiResponse<PermissionResponse> createPermission(@Valid @RequestBody PermissionRequest request) {
        ApiResponse<PermissionResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(permissionService.createPermission(request));
        apiResponse.setMessage("Permission created successfully");
        return apiResponse;
    }

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getPermissions() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getPermissions())
                .code(1000)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePermission(@PathVariable("id") String id) {
        permissionService.deletePermission(id);
        return ApiResponse.<Void>builder()
                .message("Permission deleted successfully")
                .build();
    }


}
