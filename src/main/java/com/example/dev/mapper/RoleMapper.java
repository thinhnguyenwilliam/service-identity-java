package com.example.dev.mapper;

import com.example.dev.dto.request.RoleRequest;
import com.example.dev.dto.response.RoleResponse;
import com.example.dev.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PermissionMapper.class)
public interface RoleMapper {

  @Mapping(target = "permissions", ignore = true) // mapping from request to entity
  Role toRole(RoleRequest request);

  // This will automatically map Set<Permission> → Set<PermissionResponse>
  RoleResponse toRoleResponse(Role role);
}
