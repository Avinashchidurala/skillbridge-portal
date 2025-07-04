package com.skillbridge.skillbridge_portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("SkillBridge OTP Verification");
        message.setText("Your OTP is: " + otp);
        message.setFrom("iot14041@gmail.com");
        mailSender.send(message);
    }
}
