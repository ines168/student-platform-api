package com.example.demo.service;

import com.example.demo.model.CustomUserDetails;
import com.example.demo.model.RefreshToken;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.model.dto.AuthResponse;
import com.example.demo.model.dto.LoginRequest;
import com.example.demo.model.dto.RegisterRequest;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public void register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User(
                null,
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                null,
                null,
                Role.STUDENT,
                false,
                LocalDateTime.now()
        );
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("User doesn't exist"));
//
//        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new RuntimeException("Invalid email or password");
//        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        if(authentication.isAuthenticated()) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();
            String token = jwtService.generateToken(user);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
            return new AuthResponse(token, refreshToken.getToken());
        } else {
            throw new UsernameNotFoundException("User doesn't exist");
        }

    }
}


















