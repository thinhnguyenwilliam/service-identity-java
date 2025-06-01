package com.example.dev.config;

import com.example.dev.service.ApplicationInitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ApplicationInitConfig {

  private final ApplicationInitService initService;

  @Bean
  @ConditionalOnProperty(
          prefix = "spring.datasource",
          name = "driver-class-name",
          havingValue = "com.mysql.cj.jdbc.Driver"
  )
  public ApplicationRunner applicationRunner() {
    return args -> {
      log.info("Triggering application initialization...");
      initService.initializeApplication();
    };
  }
}
