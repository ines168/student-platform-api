package com.example.demo.utils;

import java.time.LocalDateTime;

public class TokenUtil {
    public static LocalDateTime calculateExpiryDate(int minutes) {
        return LocalDateTime.now().plusMinutes(minutes);
    }
}
