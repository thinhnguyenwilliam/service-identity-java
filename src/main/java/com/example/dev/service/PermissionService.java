package com.example.dev.service;

import com.example.dev.dto.request.PermissionRequest;
import com.example.dev.dto.response.PermissionResponse;
import com.example.dev.entity.Permission;
import com.example.dev.mapper.PermissionMapper;
import com.example.dev.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse createPermission(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getPermissions() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toPermissionResponse)
                .toList();
    }

    public void deletePermission(String id) {
        permissionRepository.deleteById(id);
    }

}
