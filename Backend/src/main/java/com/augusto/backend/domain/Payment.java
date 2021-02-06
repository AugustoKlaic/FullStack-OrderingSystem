package com.augusto.backend.domain;

import com.augusto.backend.domain.enums.PaymentStateEnum;

public class Payment {

    private Integer id;
    private PaymentStateEnum paymentState;
    private Order order;

    public Payment() {
    }

    public Payment(Integer id, PaymentStateEnum paymentState, Order order) {
        this();
        this.id = id;
        this.paymentState = paymentState;
        this.order = order;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
