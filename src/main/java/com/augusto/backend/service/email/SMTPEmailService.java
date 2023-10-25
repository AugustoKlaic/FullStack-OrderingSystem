package com.augusto.backend.service.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

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
