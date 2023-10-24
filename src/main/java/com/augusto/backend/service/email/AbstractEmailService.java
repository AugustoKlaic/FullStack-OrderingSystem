package com.augusto.backend.service.email;

import com.augusto.backend.domain.PurchaseOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import java.util.Date;

public abstract class AbstractEmailService implements EmailService {

    @Value("${default.sender}")
    private String sender;

    @Override
    public void sendPurchaseOrderConfirmation(PurchaseOrder purchaseOrder) {
        SimpleMailMessage simpleMailMessage = prepareEmailMessage(purchaseOrder);
        sendEmail(simpleMailMessage);
    }

    private SimpleMailMessage prepareEmailMessage(PurchaseOrder purchaseOrder) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(purchaseOrder.getClient().getEmail());
        simpleMailMessage.setFrom(this.sender);
        simpleMailMessage.setSubject("Purchase Order confirmed!\n Order id: " + purchaseOrder.getId());
        simpleMailMessage.setSentDate(new Date(System.currentTimeMillis()));
        simpleMailMessage.setText(purchaseOrder.toString());
        return simpleMailMessage;
    }
}
