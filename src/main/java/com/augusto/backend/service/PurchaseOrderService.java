package com.augusto.backend.service;

import com.augusto.backend.domain.BilletPayment;
import com.augusto.backend.domain.Product;
import com.augusto.backend.domain.PurchaseOrder;
import com.augusto.backend.domain.enums.PaymentStateEnum;
import com.augusto.backend.repository.PaymentRepository;
import com.augusto.backend.repository.ProductRepository;
import com.augusto.backend.repository.PurchaseOrderItemRepository;
import com.augusto.backend.repository.PurchaseOrderRepository;
import com.augusto.backend.service.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final BilletPaymentService billetPaymentService;
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;

    @Autowired
    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository, BilletPaymentService billetPaymentService,
                                PaymentRepository paymentRepository, ProductRepository productRepository, PurchaseOrderItemRepository purchaseOrderItemRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.billetPaymentService = billetPaymentService;
        this.paymentRepository = paymentRepository;
        this.productRepository = productRepository;
        this.purchaseOrderItemRepository = purchaseOrderItemRepository;
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

        if (purchaseOrder.getPayment() instanceof BilletPayment) {
            BilletPayment payment = (BilletPayment) purchaseOrder.getPayment();
                billetPaymentService.fillBilletPaymentInfo(payment, purchaseOrder.getInstant());
        }

        PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(purchaseOrder);
        paymentRepository.save(purchaseOrder.getPayment());

        purchaseOrder.getItems().forEach(item -> {
            item.setDiscount(0.00);
            item.setPrice(productRepository.findById(item.getProduct().getId()).map(Product::getPrice).orElse(0.00));
            item.setPurchaseOrder(purchaseOrder);
        });
        purchaseOrderItemRepository.saveAll(purchaseOrder.getItems());
        return savedPurchaseOrder;
    }

}
