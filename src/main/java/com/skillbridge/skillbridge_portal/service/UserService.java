package com.skillbridge.skillbridge_portal.service;

import com.skillbridge.skillbridge_portal.model.User;
import com.skillbridge.skillbridge_portal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public User saveUser(User user) {
        return userRepository.save(user);
    }


    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    public void setUserActivation(Long userId, boolean isActive) {
        Optional<User> optionalUser = userRepository.findById(userId);
        optionalUser.ifPresent(user -> {
            user.setActive(isActive);
            userRepository.save(user);
        });
    }

    // 📎 Update user profile image or resume
    public void updateUserMediaUrls(Long userId, String imageUrl, String resumeUrl) {
        Optional<User> optionalUser = userRepository.findById(userId);
        optionalUser.ifPresent(user -> {
            user.setProfileImageUrl(imageUrl);
            user.setResumeUrl(resumeUrl);
            userRepository.save(user);
        });
    }
}
