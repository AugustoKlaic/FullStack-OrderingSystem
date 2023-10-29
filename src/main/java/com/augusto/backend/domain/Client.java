package com.augusto.backend.domain;

import com.augusto.backend.domain.enums.ClientProfileEnum;
import com.augusto.backend.domain.enums.ClientTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @Column(unique = true)
    private String email;
    private String nationalIdentity;
    private ClientTypeEnum clientType;

    @JsonIgnore
    private String clientPassword;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private Set<Address> addresses;

    @ElementCollection
    @CollectionTable(name = "telephone")
    private Set<String> telephones;

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    private List<PurchaseOrder> purchaseOrders;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "client_profile")
    private Set<Integer> clientProfiles;

    public Client() {
        this.clientProfiles = new HashSet<>();
        addClientProfile(ClientProfileEnum.CLIENT);
    }

    public Client(String name, String email, String nationalIdentity,
                  ClientTypeEnum clientType, Set<String> telephones,
                  List<PurchaseOrder> purchaseOrders, String clientPassword) {
        this();
        this.name = name;
        this.email = email;
        this.nationalIdentity = nationalIdentity;
        this.clientType = clientType;
        this.telephones = telephones;
        this.purchaseOrders = purchaseOrders;
        this.clientPassword = clientPassword;
        this.clientProfiles = new HashSet<>();
        addClientProfile(ClientProfileEnum.CLIENT);
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

    public String getClientPassword() {
        return clientPassword;
    }

    public void setClientPassword(String clientPassword) {
        this.clientPassword = clientPassword;
    }

    public Set<ClientProfileEnum> getClientProfiles() {
        return clientProfiles.stream().map(ClientProfileEnum::getEnum).collect(Collectors.toSet());
    }

    public void addClientProfile(ClientProfileEnum clientProfile) {
        clientProfiles.add(clientProfile.getCode());
    }
}
