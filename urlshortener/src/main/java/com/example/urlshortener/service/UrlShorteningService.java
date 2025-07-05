package com.example.urlshortener.service;

import com.example.urlshortener.model.UrlEntity;
import com.example.urlshortener.repository.UrlRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class UrlShorteningService {
    private final UrlRepository urlRepository;
    private final RedisTemplate<String, String> redisTemplate;
    
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 7;
    
    public UrlShorteningService(UrlRepository urlRepository, RedisTemplate<String, String> redisTemplate) {
        this.urlRepository = urlRepository;
        this.redisTemplate = redisTemplate;
    }
    
    public String shortenUrl(String originalUrl, Duration expiration) {
        String cachedCode = redisTemplate.opsForValue().get(originalUrl);
        if (cachedCode != null) return cachedCode;
        
        return urlRepository.findByOriginalUrl(originalUrl)
            .map(UrlEntity::getShortCode)
            .orElseGet(() -> createNewShortUrl(originalUrl, expiration));
    }
    
    private String createNewShortUrl(String originalUrl, Duration expiration) {
        String shortCode = generateUniqueCode();
        LocalDateTime now = LocalDateTime.now();
        
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setOriginalUrl(originalUrl);
        urlEntity.setShortCode(shortCode);
        urlEntity.setCreatedAt(now);
        urlEntity.setExpiresAt(now.plus(expiration));
        urlEntity.setClickCount(0);
        
        urlRepository.save(urlEntity);
        
        redisTemplate.opsForValue().set(originalUrl, shortCode, expiration);
        redisTemplate.opsForValue().set(shortCode, originalUrl, expiration);
        
        return shortCode;
    }
    
    private String generateUniqueCode() {
        Random random = new Random();
        String shortCode;
        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < CODE_LENGTH; i++) {
                sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
            }
            shortCode = sb.toString();
        } while (urlRepository.findByShortCode(shortCode).isPresent());
        
        return shortCode;
    }
    
    public String getOriginalUrl(String shortCode) {
        String cachedUrl = redisTemplate.opsForValue().get(shortCode);
        if (cachedUrl != null) return cachedUrl;
        
        UrlEntity entity = urlRepository.findByShortCode(shortCode)
            .orElseThrow(() -> new RuntimeException("URL not found"));
        
        entity.setClickCount(entity.getClickCount() + 1);
        urlRepository.save(entity);
        
        return entity.getOriginalUrl();
    }
    
    public void cleanUpExpiredUrls() {
        urlRepository.deleteAllByExpiresAtBefore(LocalDateTime.now());
    }
}