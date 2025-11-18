package com.example.demo;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.util.Properties;

@SpringBootApplication
@EnableScheduling
public class DemoApplication {

    @Value("${spring.profiles.active:local}")
    private String activeProfile;

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplication.class);

    public static void main(String[] args) {
        // Load properties from config directory
        try {
            Resource resource = new FileSystemResource("config/application.properties");
            Properties props = PropertiesLoaderUtils.loadProperties(resource);
            System.setProperty("spring.config.location", "file:config/application.properties");
        } catch (IOException e) {
            System.err.println("Error loading properties from config directory: " + e.getMessage());
        }

        SpringApplication.run(DemoApplication.class, args);
    }

    @PostConstruct
    public void init() {
        LOGGER.info("Active profile: {}", activeProfile);
    }

}
