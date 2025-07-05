package com.example.urlshortener.repository;

import com.example.urlshortener.model.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<UrlEntity, Long> {
    Optional<UrlEntity> findByShortCode(String shortCode);
    Optional<UrlEntity> findByOriginalUrl(String originalUrl);
    
    @Modifying
    @Query("DELETE FROM UrlEntity u WHERE u.expiresAt < :now")
    void deleteAllByExpiresAtBefore(LocalDateTime now);
}