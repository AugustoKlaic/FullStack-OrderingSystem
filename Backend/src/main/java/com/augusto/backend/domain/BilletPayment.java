package com.augusto.backend.domain;

import com.augusto.backend.domain.enums.PaymentStateEnum;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class BilletPayment extends Payment {

    private Date dueDate;
    private Date paymentDate;

    public BilletPayment() {
    }

    public BilletPayment(Date dueDate, Date paymentDate) {
        this();
        this.dueDate = dueDate;
        this.paymentDate = paymentDate;
    }

    public BilletPayment(Integer id, PaymentStateEnum paymentState, PurchaseOrder purchaseOrder, Date dueDate, Date paymentDate) {
        super(id, paymentState, purchaseOrder);
        this.dueDate = dueDate;
        this.paymentDate = paymentDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
}
