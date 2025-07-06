//package com.skillbridge.skillbridge_portal.controller;
//
//import com.skillbridge.skillbridge_portal.model.User;
//import com.skillbridge.skillbridge_portal.repository.UserRepository;
//import com.skillbridge.skillbridge_portal.service.UserService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//import java.util.Optional;
//
//@Tag(name = "User Controller", description = "User management APIs")
//@RestController
//@RequestMapping("/api/users")
//@CrossOrigin
//public class UserController {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @PostMapping("/register")
//    public User registerUser(@RequestBody User user) {
//        user.setVerified(false);
//        user.setActive(false);
//        user.setUniqueId("UID-" + System.currentTimeMillis());
//        return userService.saveUser(user);
//    }
//
//    @Operation(summary = "Get all users", description = "Returns all registered users")
//    @GetMapping("/all")
//    public ResponseEntity<?> getAllUsers() {
//        return ResponseEntity.ok(userRepository.findAll());
//    }
//
//
//    @GetMapping("/email/{email}")
//    public Optional<User> getUserByEmail(@PathVariable String email) {
//        return userService.findByEmail(email);
//    }
//
//
//    @PutMapping("/activate/{userId}")
//    public void setActivation(@PathVariable Long userId, @RequestParam boolean status) {
//        userService.setUserActivation(userId, status);
//    }
//
//
//    @PutMapping("/upload/{userId}")
//    public void updateMedia(@PathVariable Long userId,
//                            @RequestParam String imageUrl,
//                            @RequestParam String resumeUrl) {
//        userService.updateUserMediaUrls(userId, imageUrl, resumeUrl);
//    }
//
//    @PutMapping("/update-password/{userId}")
//    @Operation(summary = "Update password", description = "Allows Admin, Teacher, or user to securely change password")
//    public ResponseEntity<?> updatePassword(@PathVariable Long userId,
//                                            @RequestBody Map<String, String> request,
//                                            @RequestHeader("email") String requesterEmail) {
//
//        Optional<User> requesterOpt = userRepository.findByEmail(requesterEmail);
//        Optional<User> targetOpt = userRepository.findById(userId);
//
//        if (requesterOpt.isEmpty() || targetOpt.isEmpty()) {
//            return ResponseEntity.status(404).body(Map.of("message", "User or target not found"));
//        }
//
//        User requester = requesterOpt.get();
//        User target = targetOpt.get();
//
//        boolean isSelf = requester.getId().equals(target.getId());
//        boolean isAdmin = requester.getRole().equalsIgnoreCase("ADMIN");
//        boolean isTeacherModifyingStudent = requester.getRole().equalsIgnoreCase("TEACHER")
//                && target.getRole().equalsIgnoreCase("STUDENT");
//
//        if (isSelf || isAdmin || isTeacherModifyingStudent) {
//            String newPassword = request.get("newPassword");
//            target.setPassword(userService.encodePassword(newPassword));
//            userRepository.save(target);
//            return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
//        }
//
//        return ResponseEntity.status(403).body(Map.of("message", "Permission denied"));
//    }
//}
//Api documentation
package com.skillbridge.skillbridge_portal.controller;

import com.skillbridge.skillbridge_portal.model.User;
import com.skillbridge.skillbridge_portal.repository.UserRepository;
import com.skillbridge.skillbridge_portal.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Tag(name = "User Controller", description = "User management APIs")
@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        user.setVerified(false);
        user.setActive(false);
        user.setUniqueId("UID-" + System.currentTimeMillis());
        return userService.saveUser(user);
    }

    @Operation(summary = "Get all users", description = "Returns all registered users")
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
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

    @PutMapping("/update-password/{userId}")
    @Operation(summary = "Update password", description = "Allows Admin, Teacher, or user to securely change password")
    public ResponseEntity<?> updatePassword(@PathVariable Long userId,
                                            @RequestBody Map<String, String> request,
                                            @RequestHeader("email") String requesterEmail) {

        Optional<User> requesterOpt = userRepository.findByEmail(requesterEmail);
        Optional<User> targetOpt = userRepository.findById(userId);

        if (requesterOpt.isEmpty() || targetOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "User or target not found"));
        }

        User requester = requesterOpt.get();
        User target = targetOpt.get();

        boolean isSelf = requester.getId().equals(target.getId());
        boolean isAdmin = requester.getRole().equalsIgnoreCase("ADMIN");
        boolean isTeacherModifyingStudent = requester.getRole().equalsIgnoreCase("TEACHER")
                && target.getRole().equalsIgnoreCase("STUDENT");

        if (isSelf || isAdmin || isTeacherModifyingStudent) {
            String newPassword = request.get("newPassword");
            target.setPassword(userService.encodePassword(newPassword));
            userRepository.save(target);
            return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
        }

        return ResponseEntity.status(403).body(Map.of("message", "Permission denied"));
    }
}
