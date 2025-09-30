package com.example.demo.service;

import com.example.demo.model.RefreshToken;
import com.example.demo.model.User;
import com.example.demo.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${jwt.refresh-expiration-ms}")
    private Long refreshExpirationMs;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    public RefreshToken createRefreshToken(User user) {
        Optional<RefreshToken> existing = refreshTokenRepository.findByUser(user);
        existing.ifPresent(refreshTokenRepository::delete);
        RefreshToken refreshToken = new RefreshToken(
                null,
                user,
                UUID.randomUUID().toString(),
                Instant.now().plusMillis(refreshExpirationMs)
        );
        return refreshTokenRepository.save(refreshToken);
    }

    public boolean isExpired(RefreshToken refreshToken) {
        return refreshToken.getExpiryDate().isBefore(Instant.now());
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if(token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw  new RuntimeException(token.getToken() + " Refresh token is expired. Please log in.");
        }
        return token;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
