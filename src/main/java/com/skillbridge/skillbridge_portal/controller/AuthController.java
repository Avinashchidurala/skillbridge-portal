package com.skillbridge.skillbridge_portal.controller;

import com.skillbridge.skillbridge_portal.model.User;
import com.skillbridge.skillbridge_portal.repository.UserRepository;
import com.skillbridge.skillbridge_portal.security.JwtUtil;
import com.skillbridge.skillbridge_portal.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin // Allows frontend requests
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;


    private final Map<String, OTPEntry> otpMap = new ConcurrentHashMap<>();


    public static class OTPEntry {
        private final String otp;
        private final long timestamp;

        public OTPEntry(String otp, long timestamp) {
            this.otp = otp;
            this.timestamp = timestamp;
        }

        public String getOtp() { return otp; }
        public long getTimestamp() { return timestamp; }
    }


    @PostMapping("/signup")
    public String signup(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return "Email already exists.";
        }

        user.setVerified(false);
        user.setActive(false);
        user.setUniqueId("UID-" + System.currentTimeMillis());
        userRepository.save(user);

        String otp = generateOtp();
        otpMap.put(user.getEmail(), new OTPEntry(otp, System.currentTimeMillis()));
        emailService.sendOtpEmail(user.getEmail(), otp);

        return "OTP sent to your email.";
    }

    @PostMapping("/verify")
    public String verify(@RequestParam String email, @RequestParam String otp) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) return "User not found.";

        OTPEntry entry = otpMap.get(email);
        if (entry == null) return "No OTP found. Please request one.";

        long now = System.currentTimeMillis();
        if (now - entry.getTimestamp() > 5 * 60 * 1000) {
            otpMap.remove(email);
            return "OTP expired. Please request a new one.";
        }

        if (entry.getOtp().equals(otp)) {
            User user = optionalUser.get();
            user.setVerified(true);
            userRepository.save(user);
            otpMap.remove(email);
            return "OTP verified successfully!";
        } else {
            return "Incorrect OTP.";
        }
    }

    @GetMapping("/resend-otp")
    public String resendOtp(@RequestParam String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) return "Email not registered.";

        String otp = generateOtp();
        otpMap.put(email, new OTPEntry(otp, System.currentTimeMillis()));
        emailService.sendOtpEmail(email, otp);

        return "New OTP sent to your email.";
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        User user = optionalUser.get();

        if (!user.isVerified()) {
            return ResponseEntity.status(403).body("Email not verified.");
        }

        if (!user.isActive()) {
            return ResponseEntity.status(403).body("Account is deactivated. Contact administrator.");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        return ResponseEntity.ok().body(token);
    }


    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}
