package com.augusto.backend.domain;

import com.augusto.backend.domain.enums.PaymentStateEnum;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Payment {

    @Id
    private Integer id;
    private PaymentStateEnum paymentState;

    @OneToOne
    @JoinColumn(name = "purchase_order_id")
    @MapsId
    private PurchaseOrder purchaseOrder;

    public Payment() {
    }

    public Payment(Integer id, PaymentStateEnum paymentState, PurchaseOrder purchaseOrder) {
        this();
        this.id = id;
        this.paymentState = paymentState;
        this.purchaseOrder = purchaseOrder;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PaymentStateEnum getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(PaymentStateEnum paymentState) {
        this.paymentState = paymentState;
    }

    public PurchaseOrder getOrder() {
        return purchaseOrder;
    }

    public void setOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }
}
