package com.example.dev.service;

import com.example.dev.constant.PredefinedRole;
import com.example.dev.dto.request.PasswordCreationRequest;
import com.example.dev.dto.request.UserCreationRequest;
import com.example.dev.dto.request.UserUpdateRequest;
import com.example.dev.dto.response.UserResponse;
import com.example.dev.entity.Role;
import com.example.dev.entity.User;
import com.example.dev.enums.ErrorCode;
import com.example.dev.exception.AppException;
import com.example.dev.mapper.UserMapper;
import com.example.dev.repository.RoleRepository;
import com.example.dev.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
  UserRepository userRepository;
  UserMapper userMapper;
  PasswordEncoder passwordEncoder;
  RoleRepository roleRepository;

  public UserResponse createUser(UserCreationRequest request) {
    // unique field in DB so don't need to check here
//    if (userRepository.existsByUsername(request.getUsername()))
//      throw new AppException(ErrorCode.USER_EXISTED);
    //            throw new RuntimeException("ErrorCode.USER_EXISTED");

    User user = userMapper.toUser(request);
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    Set<Role> roles = new HashSet<>();
    //roles.add(Role.USER.name());
    roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);
    user.setRoles(roles);

    User savedUser = userRepository.save(user);
    return userMapper.toUserResponse(savedUser);
  }

  @PreAuthorize("hasRole('ADMIN')")
  // @PreAuthorize("hasAnyAuthority('READ_DATA', 'ADMIN', 'SUPERUSER')")
  // @PreAuthorize("hasAuthority('READ_DATA')")
  public List<UserResponse> getUsers() {
    log.info("Getting all users by admin");
    return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
  }

  @PostAuthorize("hasRole('ADMIN') or returnObject.username == authentication.name")
  public UserResponse getUser(String id) {
    return userMapper.toUserResponse(
        userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found bro")));
  }

  public UserResponse updateUser(String id, UserUpdateRequest request) {
    User user =
        userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found bro"));
    userMapper.updateUser(user, request);
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    List<com.example.dev.entity.Role> roles = roleRepository.findAllById(request.getRoles());
    user.setRoles(new HashSet<>(roles));

    return userMapper.toUserResponse(userRepository.save(user));
  }

  public void deleteUser(String id) {
    userRepository.deleteById(id);
  }

  public UserResponse getMyInfo() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();

    User user = userRepository
            .findByUsername(username)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

    UserResponse userResponse = userMapper.toUserResponse(user);

    // Set noPassword = true if password is null or blank
    userResponse.setNoPassword(!StringUtils.hasText(user.getPassword()));

    return userResponse;
  }

  public void createPassword(PasswordCreationRequest request) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userRepository
            .findByUsername(username)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

    if (StringUtils.hasText(user.getPassword())) {
      throw new AppException(ErrorCode.PASSWORD_EXISTED);
    }

    String rawPassword = request.getPassword().trim();
    user.setPassword(passwordEncoder.encode(rawPassword));
    userRepository.save(user);

    log.info("Password successfully created for user: {}", username);
  }



}
