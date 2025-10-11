package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.model.VerificationToken;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EmailVerificationService {
    private final VerificationTokenService verificationTokenService;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public String verifyEmail(String tokenString) {
        System.out.println(tokenString);
        Optional<VerificationToken> tokenOpt = verificationTokenService.getByToken(tokenString);
        if(tokenOpt.isEmpty()) {
            throw new RuntimeException("Invalid token");
        }
        VerificationToken token = tokenOpt.get();

        if(verificationTokenService.isExpired(token)) {
            verificationTokenService.delete(token);
            throw new RuntimeException("Token expired.");
        }

        User user = token.getUser();
        user.setIsEmailVerified(true);
        userRepository.save(user);

        verificationTokenService.delete(token);
        return "Email verified successfully";
    }

    public String resendVerification(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if(userOpt.isEmpty()) {
            throw new RuntimeException("Invalid email.");
        }
        User user = userOpt.get();
        if (user.getIsEmailVerified()) {
            throw new RuntimeException("Email already verified.");
        }

        verificationTokenService.deleteByUser(user); //delete existing
        VerificationToken token = verificationTokenService.createVerificationToken(user);
        emailService.verificationEmail(user.getEmail(), token.getToken());
        return "Verification email sent.";
    }
}
