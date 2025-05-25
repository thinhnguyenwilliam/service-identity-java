package com.example.dev.service;

import com.example.dev.dto.request.UserCreationRequest;
import com.example.dev.dto.response.UserResponse;
import com.example.dev.entity.User;
import com.example.dev.exception.AppException;
import com.example.dev.mapper.UserMapper;
import com.example.dev.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserCreationRequest request;
    private User userEntity;
    private UserResponse userResponse;

    @BeforeEach
    void setup() {
        request = UserCreationRequest.builder()
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .dob(LocalDate.of(1990, 1, 1))
                .password("123456789")
                .build();

        userEntity = User.builder()
                .id("cf0600f538b3")
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .dob(LocalDate.of(1990, 1, 1))
                .build();

        userResponse = UserResponse.builder()
                .id("cf0600f538b3")
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .dob("1990-01-01")
                .build();
    }

    @Test
    void createUser_validRequest_success() {
        when(userMapper.toUser(any(UserCreationRequest.class))).thenReturn(userEntity);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(userEntity);
        when(userMapper.toUserResponse(any(User.class))).thenReturn(userResponse); // 👈 this is key

        var response = userService.createUser(request);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getId()).isEqualTo("cf0600f538b3");
        Assertions.assertThat(response.getUsername()).isEqualTo("john");
    }



    @Test
    void createUser_userExisted_fail() {
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // WHEN
        AppException exception = assertThrows(AppException.class,
                () -> userService.createUser(request));

        // THEN
        Assertions.assertThat(exception).isNotNull();
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1002);
    }

}

