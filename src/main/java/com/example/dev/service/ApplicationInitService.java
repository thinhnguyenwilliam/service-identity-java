package com.example.dev.service;

import com.example.dev.constant.PredefinedRole;
import com.example.dev.entity.Role;
import com.example.dev.entity.User;
import com.example.dev.repository.RoleRepository;
import com.example.dev.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationInitService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String ADMIN_USER_NAME = "admin";
    private static final String ADMIN_PASSWORD = "A!d9sD1@e#7Gz"; // secure, random


    @Transactional
    public void initializeApplication() {
        log.info("Initializing application...");

        if (userRepository.findByUsername(ADMIN_USER_NAME).isPresent()) {
            log.info("Admin user already exists.");
            return;
        }

        try {
            // Ensure roles exist
            if (!roleRepository.existsByName(PredefinedRole.USER_ROLE)) {
                roleRepository.save(Role.builder()
                        .name(PredefinedRole.USER_ROLE)
                        .description("User role")
                        .build());
            }

            Role adminRole = roleRepository.findByName(PredefinedRole.ADMIN_ROLE)
                    .orElseGet(() -> roleRepository.save(Role.builder()
                            .name(PredefinedRole.ADMIN_ROLE)
                            .description("Admin role")
                            .build()));

            var roles = new HashSet<Role>();
            roles.add(adminRole);

            User admin = User.builder()
                    .username(ADMIN_USER_NAME)
                    .password(passwordEncoder.encode(ADMIN_PASSWORD))
                    .roles(roles)
                    .build();

            userRepository.save(admin);
            log.warn("Admin user created.");

        } catch (DataIntegrityViolationException e) {
            log.warn("Admin user already created concurrently.");
        }

        log.info("Application initialization completed.");
    }
}
