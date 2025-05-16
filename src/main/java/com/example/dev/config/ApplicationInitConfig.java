package com.example.dev.config;

import com.example.dev.entity.User;
import com.example.dev.enums.Role;
import com.example.dev.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@Slf4j
public class ApplicationInitConfig {

    @Bean
    ApplicationRunner applicationRunner(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder

    )
    {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                var roles = new HashSet<String>();
                roles.add(Role.ADMIN.name());

                User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                       // .roles(roles)
                        .build();

                userRepository.save(admin);
                log.warn("Admin user created");
            }
        };
    }
}
