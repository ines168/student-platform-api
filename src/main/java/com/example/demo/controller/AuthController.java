package com.example.demo.controller;

import com.example.demo.model.RefreshToken;
import com.example.demo.model.User;
import com.example.demo.model.dto.AuthResponse;
import com.example.demo.model.dto.LoginRequest;
import com.example.demo.model.dto.RegisterRequest;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.JwtService;
import com.example.demo.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

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
}
