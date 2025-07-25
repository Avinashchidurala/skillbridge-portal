//package com.skillbridge.skillbridge_portal.controller;
//
//import com.skillbridge.skillbridge_portal.model.User;
//import com.skillbridge.skillbridge_portal.repository.UserRepository;
//import com.skillbridge.skillbridge_portal.security.JwtUtil;
//import com.skillbridge.skillbridge_portal.service.EmailService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.http.ResponseEntity;
//
//import java.util.HashMap;
//import java.util.Optional;
//import java.util.Random;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/auth")
//@CrossOrigin // Allows frontend requests
//public class AuthController {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private EmailService emailService;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//
//    private final Map<String, OTPEntry> otpMap = new ConcurrentHashMap<>();
//
//
//    public static class OTPEntry {
//        private final String otp;
//        private final long timestamp;
//
//        public OTPEntry(String otp, long timestamp) {
//            this.otp = otp;
//            this.timestamp = timestamp;
//        }
//
//        public String getOtp() { return otp; }
//        public long getTimestamp() { return timestamp; }
//    }
//
//
//    @PostMapping("/signup")
//    public String signup(@RequestBody User user) {
//        if (user.getRole().equalsIgnoreCase("ADMIN")) {
//            Map<String, String> response = new HashMap<>();
//            response.put("message", "Admin signup is not allowed. This account must be created manually.");
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
//        }
//        if (userRepository.existsByEmail(user.getEmail())) {
//            return "Email already exists.";
//        }
//
//        user.setVerified(false);
//        user.setActive(false);
//        user.setUniqueId("UID-" + System.currentTimeMillis());
//        userRepository.save(user);
//
//        String otp = generateOtp();
//        otpMap.put(user.getEmail(), new OTPEntry(otp, System.currentTimeMillis()));
//        emailService.sendOtpEmail(user.getEmail(), otp);
//
//
//        return "OTP sent to your email.";
//    }
//
//    @PostMapping("/verify")
//    public String verify(@RequestParam String email, @RequestParam String otp) {
//        Optional<User> optionalUser = userRepository.findByEmail(email);
//        if (optionalUser.isEmpty()) return "User not found.";
//
//        OTPEntry entry = otpMap.get(email);
//        if (entry == null) return "No OTP found. Please request one.";
//
//        long now = System.currentTimeMillis();
//        if (now - entry.getTimestamp() > 5 * 60 * 1000) {
//            otpMap.remove(email);
//            return "OTP expired. Please request a new one.";
//        }
//
//        if (entry.getOtp().equals(otp)) {
//            User user = optionalUser.get();
//            user.setVerified(true);
//            userRepository.save(user);
//            otpMap.remove(email);
//            return "OTP verified successfully!";
//        } else {
//            return "Incorrect OTP.";
//        }
//    }
//
//    @GetMapping("/resend-otp")
//    public String resendOtp(@RequestParam String email) {
//        Optional<User> optionalUser = userRepository.findByEmail(email);
//        if (optionalUser.isEmpty()) return "Email not registered.";
//
//        String otp = generateOtp();
//        otpMap.put(email, new OTPEntry(otp, System.currentTimeMillis()));
//        emailService.sendOtpEmail(email, otp);
//
//        return "New OTP sent to your email.";
//    }
//
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestParam String email) {
//        Optional<User> optionalUser = userRepository.findByEmail(email);
//        if (optionalUser.isEmpty()) {
//            return ResponseEntity.badRequest().body("User not found.");
//        }
//
//        User user = optionalUser.get();
//
//        if (!user.isVerified()) {
//            return ResponseEntity.status(403).body("Email not verified.");
//        }
//
//        if (!user.isActive()) {
//            return ResponseEntity.status(403).body("Account is deactivated. Contact administrator.");
//        }
//
//        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
//
//        return ResponseEntity.ok().body(token);
//    }
//
//
//    private String generateOtp() {
//        return String.format("%06d", new Random().nextInt(999999));
//    }
//}

package com.skillbridge.skillbridge_portal.controller;

import com.skillbridge.skillbridge_portal.model.User;
import com.skillbridge.skillbridge_portal.repository.UserRepository;
import com.skillbridge.skillbridge_portal.security.JwtUtil;
import com.skillbridge.skillbridge_portal.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin // Allows frontend requests
public class AuthController {
    @Autowired
    private PasswordEncoder passwordEncoder;

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

    // 🚫 Signup restricted for ADMIN users
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        if (user.getRole().equalsIgnoreCase("ADMIN")) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Admin signup is not allowed. This account must be created manually.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Email already exists.");
            return ResponseEntity.badRequest().body(response);
        }

        user.setVerified(false);
        user.setActive(false);
        user.setUniqueId("UID-" + System.currentTimeMillis());
        userRepository.save(user);

        String otp = generateOtp();
        otpMap.put(user.getEmail(), new OTPEntry(otp, System.currentTimeMillis()));
        emailService.sendOtpEmail(user.getEmail(), otp);

        Map<String, String> response = new HashMap<>();
        response.put("message", "OTP sent to your email.");
        return ResponseEntity.ok(response);
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Email not registered."));
        }

        String otp = generateOtp();
        otpMap.put(email + ":reset", new OTPEntry(otp, System.currentTimeMillis())); // Unique key for reset
        emailService.sendOtpEmail(email, otp);

        return ResponseEntity.ok(Map.of("message", "OTP sent to your email for password reset."));
    }
    @PostMapping("/verify-reset-otp")
    public ResponseEntity<?> verifyResetOtp(@RequestParam String email, @RequestParam String otp) {
        OTPEntry entry = otpMap.get(email + ":reset");
        if (entry == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "No OTP found for reset."));
        }

        if (System.currentTimeMillis() - entry.getTimestamp() > 5 * 60 * 1000) {
            otpMap.remove(email + ":reset");
            return ResponseEntity.status(HttpStatus.GONE).body(Map.of("message", "OTP expired. Please try again."));
        }

        if (!entry.getOtp().equals(otp)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid OTP."));
        }

        otpMap.remove(email + ":reset");
        otpMap.put(email + ":verified-reset", new OTPEntry("VERIFIED", System.currentTimeMillis()));
        return ResponseEntity.ok(Map.of("message", "OTP verified. You may now reset your password."));
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String otp = payload.get("otp");
        String newPassword = payload.get("newPassword");

        OTPEntry entry = otpMap.get(email + ":reset");
        if (entry == null || !entry.getOtp().equals(otp)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid or expired OTP."));
        }

        if (System.currentTimeMillis() - entry.getTimestamp() > 5 * 60 * 1000) {
            otpMap.remove(email + ":reset");
            return ResponseEntity.status(HttpStatus.GONE).body(Map.of("message", "OTP expired."));
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "User not found."));
        }

        User user = optionalUser.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        otpMap.remove(email + ":reset");
        return ResponseEntity.ok(Map.of("message", "Password reset successful."));
    }


    // ✅ Verifies OTP within 5 minutes
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String email, @RequestParam String otp) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not found."));
        }

        OTPEntry entry = otpMap.get(email);
        if (entry == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "No OTP found. Please request one."));
        }

        long now = System.currentTimeMillis();
        if (now - entry.getTimestamp() > 5 * 60 * 1000) {
            otpMap.remove(email);
            return ResponseEntity.status(HttpStatus.GONE).body(Map.of("message", "OTP expired. Please request a new one."));
        }

        if (entry.getOtp().equals(otp)) {
            User user = optionalUser.get();
            user.setVerified(true);
            userRepository.save(user);
            otpMap.remove(email);
            return ResponseEntity.ok(Map.of("message", "OTP verified successfully!"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Incorrect OTP."));
        }
    }

    // 🔁 Request new OTP
    @GetMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestParam String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email not registered."));
        }

        String otp = generateOtp();
        otpMap.put(email, new OTPEntry(otp, System.currentTimeMillis()));
        emailService.sendOtpEmail(email, otp);

        return ResponseEntity.ok(Map.of("message", "New OTP sent to your email."));
    }

    // 🔐 JWT Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not found."));
        }

        User user = optionalUser.get();

        if (!user.isVerified()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Email not verified."));
        }

        if (!user.isActive()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Account is deactivated. Contact administrator."));
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole()); // ✅ must use .name()

        return ResponseEntity.ok(Map.of("token", token));
    }

    // 🔢 OTP Generator
    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}
