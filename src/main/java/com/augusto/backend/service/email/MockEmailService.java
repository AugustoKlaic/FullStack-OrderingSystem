package com.augusto.backend.service.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

public class MockEmailService extends AbstractEmailService {

    private final static Logger logger = LoggerFactory.getLogger(MockEmailService.class);

    @Override
    public void sendEmail(SimpleMailMessage msg) {
        logger.info("Sending mock Email\n");
        logger.info(msg.toString());
        logger.info("Email sent!");

    }
}
