package com.example.dev.controller;

import com.example.dev.dto.request.AuthenticationRequest;
import com.example.dev.dto.request.IntrospectRequest;
import com.example.dev.dto.response.ApiResponse;
import com.example.dev.dto.response.AuthenticationResponse;
import com.example.dev.dto.response.IntrospectResponse;
import com.example.dev.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        var res = authenticationService.authenticate(request);

        return ApiResponse.<AuthenticationResponse>builder()
                .code( 1000)
                .result(res)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspectToken(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException
    {
        IntrospectResponse res = authenticationService.introspect(request);

        return ApiResponse.<IntrospectResponse>builder()
                .code( 1000)
                .result(res)
                .build();
    }

}