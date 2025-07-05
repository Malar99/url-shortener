package com.example.urlshortener.task;

import com.example.urlshortener.service.UrlShorteningService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    private final UrlShorteningService urlShorteningService;

    public ScheduledTasks(UrlShorteningService urlShorteningService) {
        this.urlShorteningService = urlShorteningService;
    }

    @Scheduled(cron = "0 0 0 * * ?") // Runs at midnight every day
    public void cleanUpExpiredUrls() {
        urlShorteningService.cleanUpExpiredUrls();
        System.out.println("Cleaned up expired URLs at: " + java.time.LocalDateTime.now());
    }
}