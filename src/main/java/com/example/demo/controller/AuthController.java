package com.example.demo.controller;

import com.example.demo.model.RefreshToken;
import com.example.demo.model.User;
import com.example.demo.model.dto.AuthResponse;
import com.example.demo.model.dto.LoginRequest;
import com.example.demo.model.dto.RegisterRequest;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.service.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final EmailVerificationService emailVerificationService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody Map<String, String> request) {
        String refreshTokenString = request.get("refreshToken");

        RefreshToken token = refreshTokenRepository.findByToken(refreshTokenString)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if(refreshTokenService.isExpired(token)) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired");
        }

        String newAccessToken = jwtService.generateToken(token.getUser());
        return ResponseEntity.ok(new AuthResponse(newAccessToken, token.getToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal User user) {
        refreshTokenService.deleteByUser(user);
        return ResponseEntity.ok("Logged out successfully!");
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String tokenString) {
        return ResponseEntity.ok(emailVerificationService.verifyEmail(tokenString));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<String> verifyEmailResend(@RequestParam("email") String email) {
        return ResponseEntity.ok(emailVerificationService.resendVerification(email));
    }

    @PostMapping("/request-reset")
    public ResponseEntity<String> requestReset(@RequestParam("email") String email) {
        return ResponseEntity.ok(passwordResetService.createResetToken(email));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String password) {
        return ResponseEntity.ok(passwordResetService.resetPassword(token, password));
    }
}