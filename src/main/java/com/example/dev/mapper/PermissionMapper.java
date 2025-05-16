package com.example.dev.mapper;


import com.example.dev.dto.request.PermissionRequest;
import com.example.dev.dto.response.PermissionResponse;
import com.example.dev.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
