package com.example.urlshortener.service;

import com.example.urlshortener.model.UrlAnalyticsResponse;
import com.example.urlshortener.model.UrlEntity;
import com.example.urlshortener.repository.UrlRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AnalyticsService {
    private final UrlRepository urlRepository;

    public AnalyticsService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public UrlAnalyticsResponse getAnalytics(String shortCode) {
        Optional<UrlEntity> urlEntity = urlRepository.findByShortCode(shortCode);
        if (urlEntity.isEmpty()) {
            throw new RuntimeException("URL not found for short code: " + shortCode);
        }

        UrlEntity entity = urlEntity.get();
        return new UrlAnalyticsResponse(
            entity.getShortCode(),
            entity.getOriginalUrl(),
            entity.getClickCount(),
            entity.getCreatedAt(),
            entity.getExpiresAt(),
            LocalDateTime.now().isBefore(entity.getExpiresAt())
        );
    }
}