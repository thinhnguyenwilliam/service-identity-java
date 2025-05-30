package com.example.dev.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AnotherScheduledTask {

  @Scheduled(cron = "0 */15 * * * *") // every 15 minutes
  public void doSomething() {
    log.info("Another scheduled task is running on thread {}", Thread.currentThread().getName());
  }
}
