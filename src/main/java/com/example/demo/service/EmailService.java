package com.example.demo.service;

import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final Environment environment;

    public void sendEmailVerification(String toEmail, String verificationLink) {
        String fromEmail = environment.getProperty("spring.mail.username");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Please Verify Your Email");
        message.setText("Click the following link: " + verificationLink);
        message.setFrom(fromEmail);
        mailSender.send(message);
    }
}
