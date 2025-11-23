package com.binarybrains.userservice.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    @Value("${project.version}")
   private String version;
    @Bean
    public OpenAPI userServiceOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("User Service API")
                .description("This API handles user management operations for the scalable e-commerce platform.")
                .version(version)
                .contact(new Contact()
                    .name("untalbry")
                    .url("https://github.com/untalbry")
                )
            );
    }
}
