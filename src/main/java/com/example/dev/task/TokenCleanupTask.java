package com.example.dev.task;

import com.example.dev.repository.InvalidatedTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenCleanupTask {

    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Scheduled(cron = "0 0 * * * *") // runs every hour on the hour
    public void cleanUpExpiredTokens() {
        Date now = new Date();
        invalidatedTokenRepository.deleteByExpiryTimeBefore(now);
        log.info("Expired tokens cleaned up at {}", now);
    }
}
