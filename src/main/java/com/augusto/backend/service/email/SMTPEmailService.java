package com.augusto.backend.service.email;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class SMTPEmailService extends AbstractEmailService {

    @Override
    public void sendTextEmail(SimpleMailMessage msg) {
        javaMailSender.send(msg);
    }

    @Override
    public void sendHtmlEmail(MimeMessage msg) {
        javaMailSender.send(msg);
    }
}
