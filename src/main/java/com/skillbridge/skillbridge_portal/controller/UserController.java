package com.skillbridge.skillbridge_portal.controller;

import com.skillbridge.skillbridge_portal.model.User;
import com.skillbridge.skillbridge_portal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        // Auto-activate for test only
        user.setActive(true);
        user.setVerified(true);
        user.setUniqueId("UID-" + System.currentTimeMillis());
        return userRepository.save(user);
    }
}
