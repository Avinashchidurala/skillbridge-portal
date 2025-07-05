package com.skillbridge.skillbridge_portal;

import com.skillbridge.skillbridge_portal.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.skillbridge.skillbridge_portal.model.User;

@SpringBootApplication
public class SkillbridgePortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkillbridgePortalApplication.class, args);
	}
	@Bean
	CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder encoder) {
		return args -> {
			if (!userRepository.existsByEmail("admin@skillbridge.com")) {
				User admin = new User();
				admin.setName("System Admin");
				admin.setEmail("admin@skillbridge.com");
				admin.setPassword(encoder.encode("Admin@123"));
				admin.setRole("ADMIN");
				admin.setActive(true);
				admin.setVerified(true);
				userRepository.save(admin);
				System.out.println("✅ Default admin account created: admin@skillbridge.com");
			}
		};
	}
}
