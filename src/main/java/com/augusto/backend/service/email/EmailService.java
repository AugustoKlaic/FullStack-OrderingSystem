package com.augusto.backend.service.email;

import com.augusto.backend.domain.PurchaseOrder;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

    public void sendPurchaseOrderConfirmation(PurchaseOrder purchaseOrder);

    public void sendEmail(SimpleMailMessage msg);
}
