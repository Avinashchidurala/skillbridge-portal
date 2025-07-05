package com.skillbridge.skillbridge_portal.controller;

import com.skillbridge.skillbridge_portal.repository.UserRepository;
import com.skillbridge.skillbridge_portal.service.CertificateService;
import com.skillbridge.skillbridge_portal.model.User;
import com.skillbridge.skillbridge_portal.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/generate/{userId}")
    public ResponseEntity<Resource> generateCertificate(@PathVariable Long userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));
        String filePath = certificateService.generateCertificate(user.getName(), "SkillBridge Bootcamp");

        FileSystemResource resource = new FileSystemResource(filePath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
