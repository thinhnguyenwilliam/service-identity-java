package com.example.dev.mapper;

import com.example.dev.dto.request.RoleRequest;
import com.example.dev.dto.response.RoleResponse;
import com.example.dev.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper
{
    @Mapping(target = "permissions", ignore = true) // We will set this manually in the service
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
