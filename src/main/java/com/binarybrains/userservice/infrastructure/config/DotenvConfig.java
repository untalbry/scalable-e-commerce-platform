package com.binarybrains.userservice.infrastructure.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class DotenvConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        
        try {
            Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();
            
            Map<String, Object> dotenvProperties = new HashMap<>();
            dotenv.entries().forEach(entry -> 
                dotenvProperties.put(entry.getKey(), entry.getValue())
            );
            
            environment.getPropertySources()
                .addFirst(new MapPropertySource("dotenvProperties", dotenvProperties));
                
        } catch (Exception e) {
            // If .env file is not found, continue without it (use system environment variables)
            System.out.println("No .env file found, using system environment variables");
        }
    }
}
