package com.example.dev.controller;

import com.example.dev.dto.request.AuthenticationRequest;
import com.example.dev.dto.response.ApiResponse;
import com.example.dev.dto.response.AuthenticationResponse;
import com.example.dev.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("log-in")
    public ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        boolean isAuthenticated = authenticationService.isAuthenticated(request);

        return ApiResponse.<AuthenticationResponse>builder()
                .code(isAuthenticated ? 1000 : 9999)
                .result(AuthenticationResponse.builder()
                        .authenticated(isAuthenticated)
                        .build())
                .build();
    }

}
