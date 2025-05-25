package com.example.dev.controller;

import com.example.dev.dto.request.UserCreationRequest;
import com.example.dev.dto.response.UserResponse;
import com.example.dev.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }

    private UserCreationRequest request;
    private UserResponse userResponse;

    @BeforeEach
    void initData() {
        LocalDate dob = LocalDate.of(1990, 1, 1);

        request = UserCreationRequest.builder()
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .password("123456789")
                .dob(dob)
                .build();

        userResponse = UserResponse.builder()
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

        Mockito.when(userService.createUser(ArgumentMatchers.any()))
                .thenReturn(userResponse);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.id").value("cf0600f538b3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.username").value("john"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.lastName").value("Doe"));
    }


    @Test
    void createUser_invalidRequest_fail() throws Exception {
        request.setUsername("M"); // Assume validation requires more than 3 chars

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andDo(MockMvcResultHandlers.print()) // Helpful during debugging
                .andExpect(MockMvcResultMatchers.status().isBadRequest()) // <-- if it's a validation error
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1003))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Validation failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.username")
                        .value("Username must be at least 3 characters long hi hi"));
    }





}
