package com.example.dev.controller;

import com.example.dev.dto.request.AuthenticationRequest;
import com.example.dev.dto.request.IntrospectRequest;
import com.example.dev.dto.request.LogoutRequest;
import com.example.dev.dto.request.RefreshTokenRequest;
import com.example.dev.dto.response.ApiResponse;
import com.example.dev.dto.response.AuthenticationResponse;
import com.example.dev.dto.response.IntrospectResponse;
import com.example.dev.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import java.text.ParseException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
  AuthenticationService authenticationService;

  @PostMapping("/outbound/authentication")
  public ApiResponse<AuthenticationResponse> outboundAuthenticate(@RequestParam("code") String code) {
    AuthenticationResponse response = authenticationService.outboundAuthenticate(code);
    return ApiResponse.<AuthenticationResponse>builder()
            .code(1000)
            .result(response)
            .build();
  }

  @PostMapping("/token")
  ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
    var res = authenticationService.authenticate(request);

    return ApiResponse.<AuthenticationResponse>builder().code(1000).result(res).build();
  }

  @PostMapping("/introspect")
  ApiResponse<IntrospectResponse> introspectToken(@RequestBody IntrospectRequest request) {
    IntrospectResponse res = authenticationService.introspect(request);
    int responseCode = res.isValid() ? 1000 : 7465; // or any other error code
    return ApiResponse.<IntrospectResponse>builder()
        .code(responseCode)
        .message(res.isValid() ? "Token is valid" : "Token is invalid")
        .result(res)
        .build();
  }

  @PostMapping("/logout")
  public ApiResponse<Void> logout(@RequestBody LogoutRequest request)
      throws ParseException, JOSEException {

    authenticationService.logout(request);

    return ApiResponse.<Void>builder().code(1000).result(null).message("Logout successful").build();
  }

  @PostMapping("/refresh")
  public ApiResponse<AuthenticationResponse> authenticate(@RequestBody RefreshTokenRequest request)
      throws ParseException, JOSEException {

    return ApiResponse.<AuthenticationResponse>builder()
        .code(1000)
        .result(authenticationService.refreshToken(request))
        .message("RefreshTokenRequest successful")
        .build();
  }
}
