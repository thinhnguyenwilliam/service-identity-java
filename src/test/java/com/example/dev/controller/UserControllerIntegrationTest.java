package com.example.dev.controller;

import com.example.dev.dto.request.UserCreationRequest;
import com.example.dev.dto.response.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

  @SuppressWarnings("resource")
  @Container
  static MySQLContainer<?> mysqlContainer =
      new MySQLContainer<>("mysql:8.0")
          .withDatabaseName("identity_service")
          .withUsername("root")
          .withPassword("1234");

  @DynamicPropertySource
  static void overrideProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
    registry.add("spring.datasource.username", mysqlContainer::getUsername);
    registry.add("spring.datasource.password", mysqlContainer::getPassword);
    registry.add("spring.datasource.driver-class-name", mysqlContainer::getDriverClassName);
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
  }

  @Autowired private MockMvc mockMvc;

  private UserCreationRequest request;
  private UserResponse userResponse;

  @BeforeEach
  void initData() {
    LocalDate dob = LocalDate.of(1990, 1, 1);

    request =
        UserCreationRequest.builder()
            .username("john")
            .firstName("John")
            .lastName("Doe")
            .password("123456789")
            .dob(dob)
            .build();

    userResponse =
        UserResponse.builder()
            .id("cf0600f538b3")
            .username("john")
            .firstName("John")
            .lastName("Doe")
            .dob(String.valueOf(dob))
            .build();
  }

  @Test
  void createUser_validRequest_success() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    String content = objectMapper.writeValueAsString(request);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1000))
        .andExpect(MockMvcResultMatchers.jsonPath("$.result.username").value("john"));
  }
}
