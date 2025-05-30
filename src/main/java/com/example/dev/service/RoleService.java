package com.example.dev.service;

import com.example.dev.dto.request.RoleRequest;
import com.example.dev.dto.response.RoleResponse;
import com.example.dev.entity.Permission;
import com.example.dev.entity.Role;
import com.example.dev.mapper.RoleMapper;
import com.example.dev.repository.PermissionRepository;
import com.example.dev.repository.RoleRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleService {
  RoleRepository roleRepository;
  PermissionRepository permissionRepository;
  RoleMapper roleMapper;

  public RoleResponse createRole(RoleRequest request) {
    Role role = roleMapper.toRole(request);

    // Convert permission names to Permission entities
    Set<Permission> permissions =
        request.getPermissions().stream()
            .map(
                name ->
                    permissionRepository
                        .findById(name)
                        .orElseThrow(
                            () -> new IllegalArgumentException("Permission not found: " + name)))
            .collect(Collectors.toSet());

    role.setPermissions(permissions);

    roleRepository.save(role);
    return roleMapper.toRoleResponse(role);
  }

  public List<RoleResponse> getAllRoles() {
    return roleRepository.findAll().stream()
        .map(roleMapper::toRoleResponse)
        .collect(Collectors.toList());
  }

  public void deleteRole(String roleName) {
    if (!roleRepository.existsById(roleName)) {
      throw new IllegalArgumentException("Role not found: " + roleName);
    }

    roleRepository.deleteById(roleName);
  }
}
