package com.augusto.backend.domain;

import com.augusto.backend.domain.enums.PaymentStateEnum;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.Entity;

@Entity
@JsonTypeName("creditCardPayment")
public class CreditCardPayment extends Payment {

    private Integer installmentsNumber;

    public CreditCardPayment() {
    }

    public CreditCardPayment(Integer installmentsNumber) {
        this();
        this.installmentsNumber = installmentsNumber;
    }

    public CreditCardPayment(Integer id, PaymentStateEnum paymentState, PurchaseOrder purchaseOrder, Integer installmentsNumber) {
        super(id, paymentState, purchaseOrder);
        this.installmentsNumber = installmentsNumber;
    }

    public Integer getInstallmentsNumber() {
        return installmentsNumber;
    }

    public void setInstallmentsNumber(Integer installmentsNumber) {
        this.installmentsNumber = installmentsNumber;
    }
}
