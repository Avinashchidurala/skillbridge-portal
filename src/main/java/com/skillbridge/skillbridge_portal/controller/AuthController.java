package com.skillbridge.skillbridge_portal.controller;
import com.skillbridge.skillbridge_portal.model.User;
import com.skillbridge.skillbridge_portal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public String signup(@RequestBody User user) {
        // Generate OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        user.setOtpCode(otp);
        user.setVerified(false);
        user.setActive(false);
        user.setUniqueId("UID-" + System.currentTimeMillis());

        userRepository.save(user);

        System.out.println("OTP for " + user.getEmail() + ": " + otp);
        return "OTP sent to your email";
    }

    @PostMapping("/verify")
    public String verifyOtp(@RequestParam String email, @RequestParam String otp) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getOtpCode().equals(otp)) {
                user.setVerified(true);
                userRepository.save(user);
                return "OTP verified successfully!";
            } else {
                return "Invalid OTP.";
            }
        } else {
            return "User not found.";
        }
    }
}

