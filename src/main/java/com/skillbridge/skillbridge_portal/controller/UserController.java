package com.skillbridge.skillbridge_portal.controller;

import com.skillbridge.skillbridge_portal.model.User;
import com.skillbridge.skillbridge_portal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        user.setVerified(false);
        user.setActive(false);
        user.setUniqueId("UID-" + System.currentTimeMillis());
        return userService.saveUser(user);
    }

    @GetMapping("/email/{email}")
    public Optional<User> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }

    @PutMapping("/activate/{userId}")
    public void setActivation(@PathVariable Long userId, @RequestParam boolean status) {
        userService.setUserActivation(userId, status);
    }

    @PutMapping("/upload/{userId}")
    public void updateMedia(@PathVariable Long userId,
                            @RequestParam String imageUrl,
                            @RequestParam String resumeUrl) {
        userService.updateUserMediaUrls(userId, imageUrl, resumeUrl);
    }
}
