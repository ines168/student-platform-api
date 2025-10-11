package com.example.demo.service;

import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@AllArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final Environment environment;

    public void verificationEmail(String email, String verificationToken) {
        String subject = "Please Verify Your Email";
        String path = "/api/auth/verify-email";
        String message = "Click the button below to verify your mail:";
        sendEmail(email, verificationToken, subject, path, message);
    }

    public void passwordEmail(String email, String resetToken) {
        String subject = "Password Reset Request";
        String path = "/api/auth/reset-password";
        String message = "Click the button below to reset your password:";
        sendEmail(email, resetToken, subject, path, message);
    }

    public void sendEmail(String email, String token, String subject, String path, String message) {
        try {
            String fromEmail = environment.getProperty("spring.mail.username");
            String actionUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(path)
                    .queryParam("token", token)
                    .toUriString();

            String content = """
                    <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border-radius: 8px; background-color: #f9f9f9; text-align: center;">
                        <h2 style="color: #333;">%s</h2>
                        <p style="font-size: 16px; color: #555;">%s</p>
                        <a href="%s" style="display: inline-block; margin: 28px 0; padding: 10px 20px; font-size: 16px; color: #fff; background-color: #007bff; text-decoration: none; border-radius: 5px;">Proceed</a>
                        <p style="font-size: 14px; color: "777;">Or copy and paste this link into your browser:</p>
                        <p style="font-size: 14px; color: "007bff">%s</p>
                        <p style="font-size: 12px; color: #aaa;">This is an automated message. Please do not reply.</p>
                    </div>""".formatted(subject, message, actionUrl, actionUrl);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setFrom(fromEmail);
            helper.setText(content, true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            System.err.println("Failed to send email. " + e.getMessage());
        }
    }
}
