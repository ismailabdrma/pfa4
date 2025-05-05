package com.amn.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otpCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("🔐 Votre code de vérification OTP - AMN");
        message.setText("Voici votre code OTP : " + otpCode + "\nIl est valable pendant 5 minutes.\n\nMerci,\nEquipe AMN");
        mailSender.send(message);
    }
}
