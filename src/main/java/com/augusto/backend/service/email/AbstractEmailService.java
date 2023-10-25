package com.augusto.backend.service.email;

import com.augusto.backend.domain.PurchaseOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Service
public abstract class AbstractEmailService implements EmailService {

    private final static String TEMPLATE_PURCHASE_ORDER_CONFIRMATION_HTML = "email/purchaseOrderConfirmation";

    @Value("${default.sender}")
    private String sender;

    private final TemplateEngine templateEngine;
    protected final JavaMailSender javaMailSender;

    @Autowired
    public AbstractEmailService(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendPurchaseOrderConfirmationTextEmail(PurchaseOrder purchaseOrder) {
        SimpleMailMessage simpleMailMessage = prepareEmailTextMessage(purchaseOrder);
        sendTextEmail(simpleMailMessage);
    }

    @Override
    public void sendPurchaseOrderConfirmationHtmlEmail(PurchaseOrder purchaseOrder){
        try {
            MimeMessage mimeMessage = prepareEmailHtmlMessage(purchaseOrder);
            sendHtmlEmail(mimeMessage);
        } catch (MessagingException e) {
            sendPurchaseOrderConfirmationTextEmail(purchaseOrder);
        }
    }

    private MimeMessage prepareEmailHtmlMessage(PurchaseOrder purchaseOrder) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(purchaseOrder.getClient().getEmail());
        mimeMessageHelper.setFrom(sender);
        mimeMessageHelper.setSubject("Purchase Order confirmed!\n Order id: " + purchaseOrder.getId());
        mimeMessageHelper.setSentDate(new Date(System.currentTimeMillis()));
        mimeMessageHelper.setText(createCompleteHtmlForEmail(purchaseOrder), true);
        return mimeMessage;
    }

    private SimpleMailMessage prepareEmailTextMessage(PurchaseOrder purchaseOrder) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(purchaseOrder.getClient().getEmail());
        simpleMailMessage.setFrom(this.sender);
        simpleMailMessage.setSubject("Purchase Order confirmed!\n Order id: " + purchaseOrder.getId());
        simpleMailMessage.setSentDate(new Date(System.currentTimeMillis()));
        simpleMailMessage.setText(purchaseOrder.toString());
        return simpleMailMessage;
    }

    private String createCompleteHtmlForEmail(PurchaseOrder purchaseOrder) {
        Context context = new Context();
        context.setVariable("purchaseOrder", purchaseOrder);
        return templateEngine.process(TEMPLATE_PURCHASE_ORDER_CONFIRMATION_HTML, context);
    }
}
