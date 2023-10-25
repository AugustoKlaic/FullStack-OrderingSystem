package com.augusto.backend.service.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import javax.mail.internet.MimeMessage;

@Service
public class SMTPEmailService extends AbstractEmailService {

    public SMTPEmailService(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
        super(templateEngine, javaMailSender);
    }

    @Override
    public void sendTextEmail(SimpleMailMessage msg) {
        javaMailSender.send(msg);
    }

    @Override
    public void sendHtmlEmail(MimeMessage msg) {
        javaMailSender.send(msg);
    }
}
