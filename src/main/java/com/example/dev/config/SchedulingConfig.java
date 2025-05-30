package com.example.dev.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulingConfig {

  @Bean(destroyMethod = "shutdown")
  public ThreadPoolTaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setPoolSize(5); // Allows up to 5 tasks to run concurrently
    scheduler.setThreadNamePrefix("sched-");
    scheduler.setRemoveOnCancelPolicy(true);
    scheduler.initialize();
    return scheduler;
  }
}
