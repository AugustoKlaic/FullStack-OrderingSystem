package com.augusto.backend.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

@Entity
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date instant;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "purchaseOrder")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "delivery_address_id")
    private Address address;

    @OneToMany(mappedBy = "id.purchaseOrder")
    private Set<PurchaseOrderItem> items;

    public PurchaseOrder() {
    }

    public PurchaseOrder(Integer id, Date instant, Payment payment, Client client, Address address, Set<PurchaseOrderItem> items) {
        this();
        this.id = id;
        this.instant = instant;
        this.payment = payment;
        this.client = client;
        this.address = address;
        this.items = items;
    }

    public Double getTotalOrderPrice() {
        return this.items.stream()
                .map(PurchaseOrderItem::getOrderSubTotal)
                .reduce(0.00, Double::sum);
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

    public Set<PurchaseOrderItem> getItems() {
        return items;
    }

    public void setItems(Set<PurchaseOrderItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        final StringBuilder sb = new StringBuilder();
        sb.append("Purchase Order number: ").append(getId());
        sb.append(", date: ").append(simpleDateFormat.format(getInstant()));
        sb.append(", client name: ").append(getClient().getName());
        sb.append(", payment status: ").append(getPayment().getPaymentState());
        sb.append("\ndetails: \n");
        getItems().forEach(item -> sb.append(item.toString()));
        sb.append("\nTotal: ").append(numberFormat.format(getTotalOrderPrice()));
        return sb.toString();
    }
}
