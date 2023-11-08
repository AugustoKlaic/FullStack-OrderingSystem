package com.augusto.backend.service;

import com.augusto.backend.domain.BilletPayment;
import com.augusto.backend.domain.Client;
import com.augusto.backend.domain.Product;
import com.augusto.backend.domain.PurchaseOrder;
import com.augusto.backend.domain.enums.PaymentStateEnum;
import com.augusto.backend.repository.PaymentRepository;
import com.augusto.backend.repository.PurchaseOrderItemRepository;
import com.augusto.backend.repository.PurchaseOrderRepository;
import com.augusto.backend.service.email.EmailService;
import com.augusto.backend.service.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final BilletPaymentService billetPaymentService;
    private final PaymentRepository paymentRepository;
    private final ProductService productService;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final ClientService clientService;
    private final EmailService emailService;

    @Autowired
    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository, BilletPaymentService billetPaymentService,
                                PaymentRepository paymentRepository, ProductService productService, PurchaseOrderItemRepository purchaseOrderItemRepository,
                                ClientService clientService, EmailService emailService) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.billetPaymentService = billetPaymentService;
        this.paymentRepository = paymentRepository;
        this.productService = productService;
        this.purchaseOrderItemRepository = purchaseOrderItemRepository;
        this.clientService = clientService;
        this.emailService = emailService;
    }

    public List<PurchaseOrder> findAllPurchaseOrders(String clientEmail) {
        Client client = clientService.findByEmail(clientEmail);
        return purchaseOrderRepository.findAllPurchaseOrdersByClient(client.getId());
    }

    public PurchaseOrder findById(final Integer id) {
        return purchaseOrderRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Purchase Order not found for Id: " + id));
    }

    @Transactional
    public PurchaseOrder create(PurchaseOrder purchaseOrder) {
        purchaseOrder.setId(null);
        purchaseOrder.setInstant(new Date());
        purchaseOrder.getPayment().setPaymentState(PaymentStateEnum.PENDING);
        purchaseOrder.getPayment().setOrder(purchaseOrder);
        purchaseOrder.setClient(clientService.findById(purchaseOrder.getClient().getId()));

        if (purchaseOrder.getPayment() instanceof BilletPayment) {
            BilletPayment payment = (BilletPayment) purchaseOrder.getPayment();
                billetPaymentService.fillBilletPaymentInfo(payment, purchaseOrder.getInstant());
        }

        PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(purchaseOrder);
        paymentRepository.save(purchaseOrder.getPayment());

        purchaseOrder.getItems().forEach(item -> {
            Product product = productService.findById(item.getProduct().getId());
            item.setDiscount(0.00);
            item.setProduct(product);
            item.setPrice(product.getPrice());
            item.setPurchaseOrder(purchaseOrder);
        });
        purchaseOrderItemRepository.saveAll(purchaseOrder.getItems());
        emailService.sendPurchaseOrderConfirmationHtmlEmail(purchaseOrder);
        return savedPurchaseOrder;
    }

}
