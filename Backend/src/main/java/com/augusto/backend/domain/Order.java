package com.augusto.backend.domain;

import java.util.Date;

public class Order {

    private Integer id;
    private Date instant;
    private Payment payment;
    private Client client;
    private Address address;

    public Order() {
    }

    public Order(Integer id, Date instant, Payment payment, Client client, Address address) {
        this();
        this.id = id;
        this.instant = instant;
        this.payment = payment;
        this.client = client;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getInstant() {
        return instant;
    }

    public void setInstant(Date instant) {
        this.instant = instant;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
