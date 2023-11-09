package com.augusto.backend.service.email;

import com.augusto.backend.domain.Client;
import com.augusto.backend.domain.PurchaseOrder;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;


public interface EmailService {

    public void sendPurchaseOrderConfirmationTextEmail(PurchaseOrder purchaseOrder);

    public void sendTextEmail(SimpleMailMessage msg);

    public void sendPurchaseOrderConfirmationHtmlEmail(PurchaseOrder purchaseOrder);

    public void sendHtmlEmail(MimeMessage msg);

    void sendPasswordRecoveryEmail(Client client, String newPassword);
}
