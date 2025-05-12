package com.example.dev.service;

import com.example.dev.dto.request.AuthenticationRequest;
import com.example.dev.entity.User;
import com.example.dev.enums.ErrorCode;
import com.example.dev.exception.AppException;
import com.example.dev.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public boolean isAuthenticated(AuthenticationRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return passwordEncoder.matches(request.getPassword(), user.getPassword());
    }
}
