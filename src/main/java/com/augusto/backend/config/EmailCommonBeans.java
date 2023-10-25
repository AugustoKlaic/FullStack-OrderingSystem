package com.augusto.backend.config;

import com.augusto.backend.service.email.EmailService;
import com.augusto.backend.service.email.SMTPEmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.TemplateEngine;

@Configuration
public class EmailCommonBeans {

    @Bean
    public EmailService emailService(){
        return new SMTPEmailService(templateEngine(), javaMailSender());
    }

    @Bean
    public TemplateEngine templateEngine() {
        return new TemplateEngine();
    }

    @Bean
    public JavaMailSender javaMailSender() {
        return new JavaMailSenderImpl();
    }
}
