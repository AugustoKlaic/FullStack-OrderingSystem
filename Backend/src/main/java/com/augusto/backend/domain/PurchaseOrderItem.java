package com.augusto.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class PurchaseOrderItem {

    @JsonIgnore
    @EmbeddedId
    private PurchaseOrderItemId id = new PurchaseOrderItemId();

    private Double discount;
    private Integer quantity;
    private Double price;

    public PurchaseOrderItem() {
    }

    public PurchaseOrderItem(PurchaseOrder purchaseOrder, Product product, Double discount, Integer quantity, Double price) {
        this();
        this.id.setPurchaseOrder(purchaseOrder);
        this.id.setProduct(product);
        this.discount = discount;
        this.quantity = quantity;
        this.price = price;
    }

    public Double getOrderSubTotal() {
        return (price - discount) * quantity;
    }

    @JsonIgnore
    public PurchaseOrder getPurchaseOrder() {
        return id.getPurchaseOrder();
    }

    public Product getProduct() {
        return id.getProduct();
    }

    public PurchaseOrderItemId getId() {
        return id;
    }

    public void setId(PurchaseOrderItemId id) {
        this.id = id;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
