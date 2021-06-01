package com.augusto.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Double price;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "product_category",
            joinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id", referencedColumnName = "id")})
    private List<Category> categoryList;

    @OneToMany(mappedBy = "id.product")
    @JsonIgnore
    private Set<PurchaseOrderItem> items;

    public Product() {
    }

    public Product(Integer id, String name, Double price, List<Category> categoryList, Set<PurchaseOrderItem> items) {
        this();
        this.id = id;
        this.name = name;
        this.price = price;
        this.categoryList = categoryList;
        this.items = items;
    }

    @JsonIgnore
    public List<PurchaseOrder> getPurchaseOrders() {
        return this.items.stream().map(PurchaseOrderItem::getPurchaseOrder)
                .collect(Collectors.toList());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public Set<PurchaseOrderItem> getItems() {
        return items;
    }

    public void setItems(Set<PurchaseOrderItem> items) {
        this.items = items;
    }
}
