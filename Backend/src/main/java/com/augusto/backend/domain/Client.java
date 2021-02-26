package com.augusto.backend.domain;

import com.augusto.backend.domain.enums.ClientTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String email;
    private String nationalIdentity;
    private ClientTypeEnum clientType;

    @OneToMany(mappedBy = "client")
    private Set<Address> addresses;

    @ElementCollection
    @CollectionTable(name = "telephone")
    private Set<String> telephones;

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    private List<PurchaseOrder> purchaseOrders;

    public Client() {
    }

    public Client(Integer id, String name, String email, String nationalIdentity, ClientTypeEnum clientType, Set<Address> addresses, Set<String> telephones, List<PurchaseOrder> purchaseOrders) {
        this();
        this.id = id;
        this.name = name;
        this.email = email;
        this.nationalIdentity = nationalIdentity;
        this.clientType = clientType;
        this.addresses = addresses;
        this.telephones = telephones;
        this.purchaseOrders = purchaseOrders;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNationalIdentity() {
        return nationalIdentity;
    }

    public void setNationalIdentity(String nationalIdentity) {
        this.nationalIdentity = nationalIdentity;
    }

    public ClientTypeEnum getClientType() {
        return clientType;
    }

    public void setClientType(ClientTypeEnum clientType) {
        this.clientType = clientType;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public Set<String> getTelephones() {
        return telephones;
    }

    public void setTelephones(Set<String> telephones) {
        this.telephones = telephones;
    }

    public List<PurchaseOrder> getPurchaseOrders() {
        return purchaseOrders;
    }

    public void setPurchaseOrders(List<PurchaseOrder> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }
}
