package com.example.dev.mapper;

import com.example.dev.dto.request.UserCreationRequest;
import com.example.dev.dto.request.UserUpdateRequest;
import com.example.dev.dto.response.UserResponse;
import com.example.dev.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    //@Mapping(source = "firstName", target = "lastName")
    //@Mapping(target = "firstName", ignore = true)// no mapping for an attribute firstName
    UserResponse toUserResponse(User user);
}
