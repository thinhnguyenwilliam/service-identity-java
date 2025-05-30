package com.example.dev.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.dev.dto.request.UserCreationRequest;
import com.example.dev.dto.response.UserResponse;
import com.example.dev.entity.User;
import com.example.dev.exception.AppException;
import com.example.dev.mapper.UserMapper;
import com.example.dev.repository.UserRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:test.yml")
class UserServiceTest {

  @Mock private UserRepository userRepository;

  @Mock private UserMapper userMapper;

  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private UserService userService;

  private UserCreationRequest request;
  private User userEntity;
  private UserResponse userResponse;

  @BeforeEach
  void setup() {
    request =
        UserCreationRequest.builder()
            .username("john")
            .firstName("John")
            .lastName("Doe")
            .dob(LocalDate.of(1990, 1, 1))
            .password("123456789")
            .build();

    userEntity =
        User.builder()
            .id("cf0600f538b3")
            .username("john")
            .firstName("John")
            .lastName("Doe")
            .dob(LocalDate.of(1990, 1, 1))
            .build();

    userResponse =
        UserResponse.builder()
            .id("cf0600f538b3")
            .username("john")
            .firstName("John")
            .lastName("Doe")
            .dob("1990-01-01")
            .build();
  }

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
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
    AppException exception =
        assertThrows(AppException.class, () -> userService.createUser(request));

    // THEN
    Assertions.assertThat(exception).isNotNull();
    Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1002);
  }

  @Test
  void getMyInfo_shouldReturnUserResponse_whenUserExists() {
    // Arrange

    // 1. Mock SecurityContextHolder
    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn("testuser");

    SecurityContext securityContext = mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);

    // 2. Mock repository and mapper
    when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(userEntity));
    when(userMapper.toUserResponse(userEntity)).thenReturn(userResponse);

    // Act
    UserResponse response = userService.getMyInfo();

    // Assert
    Assertions.assertThat(response.getId()).isEqualTo("cf0600f538b3");
    Assertions.assertThat(response.getUsername()).isEqualTo("john");
  }

  //    @Test
  //    void getMyInfo_shouldThrowException_whenUserNotFound() {
  //        // Arrange
  //        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
  //
  //        // Act & Assert
  //        AppException exception = assertThrows(AppException.class, () ->
  // userService.getMyInfo());
  //        assertEquals(ErrorCode.USER_NOT_EXISTED, exception.getErrorCode());
  //    }
}
