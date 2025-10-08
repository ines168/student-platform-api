package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.model.VerificationToken;
import com.example.demo.repository.VerificationTokenRepository;
import com.example.demo.utils.TokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final int EXPIRATION_MINUTES = 60 * 24;

    public VerificationToken createVerificationToken(User user) {
        verificationTokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(
                null,
                token,
                user,
                TokenUtil.calculateExpiryDate(EXPIRATION_MINUTES)
        );
        return verificationTokenRepository.save(verificationToken);
    }

    public Optional<VerificationToken> getByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    public boolean isExpired(VerificationToken token) {
        return token.getExpiryDate().isBefore(LocalDateTime.now());
    }

    public void delete(VerificationToken token) {
        verificationTokenRepository.delete(token);
    }

    @Transactional
    public void deleteByUser(User user) {
        verificationTokenRepository.deleteByUser(user);
    }


}


















