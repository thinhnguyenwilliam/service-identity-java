package com.example.dev.config;

import com.example.dev.dto.response.ApiResponse;
import com.example.dev.enums.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {

    ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;

    // Build custom response
    ApiResponse<?> apiResponse =
        ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build();

    // Set response headers
    response.setStatus(errorCode.getStatusCode().value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    // Write JSON response body
    objectMapper.writeValue(response.getWriter(), apiResponse);
  }
}
