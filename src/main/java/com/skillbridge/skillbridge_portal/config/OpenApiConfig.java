package com.skillbridge.skillbridge_portal.config;

import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI skillBridgeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SkillBridge API Documentation")
                        .description("Secure RESTful API for IT training platform")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Avinash (Developer)")
                                .email("your-email@example.com")
                        )
                );
    }
}
