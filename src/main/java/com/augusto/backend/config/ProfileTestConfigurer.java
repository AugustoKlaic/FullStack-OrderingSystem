package com.augusto.backend.config;

import com.augusto.backend.config.service.CreateTestDatabase;
import com.augusto.backend.service.email.EmailService;
import com.augusto.backend.service.email.MockEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.TemplateEngine;

import java.text.ParseException;

@Configuration
@Profile("test")
public class ProfileTestConfigurer {

    private final CreateTestDatabase createTestDatabase;

    @Autowired
    public ProfileTestConfigurer(CreateTestDatabase createTestDatabase) {
        this.createTestDatabase = createTestDatabase;
    }

    @Bean
    public boolean instantiateDatabase() throws ParseException {

        createTestDatabase.instantiateTestDatabase();
        return true;
    }

    @Bean
    public EmailService emailService() {
        return new MockEmailService();
    }

    @Bean
    public JavaMailSender javaMailSender() {
        return new JavaMailSenderImpl();
    }
}
