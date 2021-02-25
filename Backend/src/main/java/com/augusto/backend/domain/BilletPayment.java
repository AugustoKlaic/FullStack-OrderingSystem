package com.augusto.backend.domain;

import com.augusto.backend.domain.enums.PaymentStateEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class BilletPayment extends Payment {

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dueDate;

    @JsonFormat(pattern = "dd/MM/yyyy")
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
