package com.augusto.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

import java.text.NumberFormat;
import java.util.Locale;

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

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        id.setPurchaseOrder(purchaseOrder);
    }

    public Product getProduct() {
        return id.getProduct();
    }

    public void setProduct(Product product) {
        id.setProduct(product);
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

    @Override
    public String toString() {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        final StringBuilder builder = new StringBuilder();
        builder.append(getProduct().getName());
        builder.append(", Quantity: ").append(getQuantity());
        builder.append(", Price: ").append(numberFormat.format(getPrice()));
        builder.append(", Subtotal: ").append(numberFormat.format(getOrderSubTotal()));
        builder.append("\n");
        return builder.toString();
    }
}
