package com.smarthome.energy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OtpService {

    private static final int OTP_VALIDITY_MINUTES = 5;

    @Autowired
    private JavaMailSender mailSender;

    private final Map<String, OtpDetails> otpStorage = new HashMap<>();
    private final Map<String, Boolean> verifiedEmails = new HashMap<>();

    public String sendOtp(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        Instant expiresAt = Instant.now().plusSeconds(OTP_VALIDITY_MINUTES * 60L);
        otpStorage.put(email, new OtpDetails(otp, expiresAt));
        verifiedEmails.remove(email);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Smart Home Energy - OTP Verification");
        message.setText(
                "Hello,\n\n" +
                "This OTP is for Smart Home Energy Management verification.\n" +
                "Your OTP is: " + otp + "\n" +
                "This OTP is valid for " + OTP_VALIDITY_MINUTES + " minutes.\n\n" +
                "If you did not request this, please ignore this email.\n\n" +
                "Regards,\n" +
                "Smart Home Energy Team"
        );

        try {
            mailSender.send(message);
            return "OTP sent successfully";
        } catch (MailException ex) {
            throw new IllegalStateException("Failed to send OTP email. Check SMTP username and app password.");
        }
    }

    public boolean verifyOtp(String email, String otp) {
        if (email == null || email.isBlank() || otp == null || otp.isBlank()) {
            return false;
        }

        OtpDetails details = otpStorage.get(email);
        if (details == null) {
            return false;
        }

        if (Instant.now().isAfter(details.expiresAt())) {
            otpStorage.remove(email);
            return false;
        }

        boolean valid = details.code().equals(otp.trim());
        if (valid) {
            otpStorage.remove(email);
            verifiedEmails.put(email, true);
        }

        return valid;
    }

    public boolean isEmailVerified(String email) {
        return Boolean.TRUE.equals(verifiedEmails.get(email));
    }

    public void clearVerification(String email) {
        verifiedEmails.remove(email);
    }

    private record OtpDetails(String code, Instant expiresAt) {
    }
}
