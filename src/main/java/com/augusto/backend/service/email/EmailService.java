package com.augusto.backend.service.email;

import com.augusto.backend.domain.PurchaseOrder;
import org.springframework.mail.SimpleMailMessage;

import javax.mail.internet.MimeMessage;

public interface EmailService {

    public void sendPurchaseOrderConfirmationTextEmail(PurchaseOrder purchaseOrder);

    public void sendTextEmail(SimpleMailMessage msg);

    public void sendPurchaseOrderConfirmationHtmlEmail(PurchaseOrder purchaseOrder);

    public void sendHtmlEmail(MimeMessage msg);
}
