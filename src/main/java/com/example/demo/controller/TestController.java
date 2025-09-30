package com.example.demo.controller;

import com.example.demo.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/me")
    public ResponseEntity<String> getMe(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok("Hello, " + user.getEmail());
    }

}
