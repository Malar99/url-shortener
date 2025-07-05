package com.example.urlshortener.controller;

import com.example.urlshortener.model.UrlAnalyticsResponse;
import com.example.urlshortener.service.AnalyticsService;
import com.example.urlshortener.service.UrlShorteningService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.time.Duration;

@RestController
@RequestMapping("/api/url")
public class UrlController {
    private final UrlShorteningService urlShorteningService;
    private final AnalyticsService analyticsService;

    public UrlController(UrlShorteningService urlShorteningService, AnalyticsService analyticsService) {
        this.urlShorteningService = urlShorteningService;
        this.analyticsService = analyticsService;
    }
    
    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(
            @RequestParam String originalUrl,
            @RequestParam(defaultValue = "30") int daysToExpire) {
        try {
            String shortCode = urlShorteningService.shortenUrl(
                originalUrl, 
                Duration.ofDays(daysToExpire)
            );
            return ResponseEntity.ok("http://localhost:8080/api/url/" + shortCode);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error shortening URL: " + e.getMessage());
        }
    }
    
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(
            @PathVariable String shortCode, 
            HttpServletResponse response) throws IOException {
        try {
            String originalUrl = urlShorteningService.getOriginalUrl(shortCode);
            response.sendRedirect(originalUrl);
            return ResponseEntity.status(HttpStatus.FOUND).build();
        } catch (RuntimeException e) {
            response.sendError(HttpStatus.NOT_FOUND.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @GetMapping("/{shortCode}/analytics")
    public ResponseEntity<UrlAnalyticsResponse> getAnalytics(
            @PathVariable String shortCode) {
        try {
            return ResponseEntity.ok(analyticsService.getAnalytics(shortCode));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}