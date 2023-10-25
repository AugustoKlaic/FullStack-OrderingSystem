package com.augusto.backend.service.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import javax.mail.internet.MimeMessage;

@Service
public class MockEmailService extends AbstractEmailService {

    private final static Logger logger = LoggerFactory.getLogger(MockEmailService.class);

    public MockEmailService(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
        super(templateEngine, javaMailSender);
    }

    @Override
    public void sendTextEmail(SimpleMailMessage msg) {
        logger.info("Sending mock Email\n");
        logger.info(msg.toString());
        logger.info("Email sent!");

    }

    @Override
    public void sendHtmlEmail(MimeMessage msg) {
        logger.info("Sending mock HTML Email\n");
        logger.info(msg.toString());
        logger.info("Email sent!");
    }
}
