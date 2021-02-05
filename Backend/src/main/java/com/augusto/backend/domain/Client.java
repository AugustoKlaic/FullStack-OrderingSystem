package com.augusto.backend.domain;

import com.augusto.backend.domain.enums.ClientTypeEnum;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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

    @JsonManagedReference
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private List<Address> addresses;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "telephone")
    private Set<String> telephones;

    public Client() {
    }

    public Client(Integer id, String name, String email, String nationalIdentity, ClientTypeEnum clientType, List<Address> addresses, Set<String> telephones) {
        this();
        this.id = id;
        this.name = name;
        this.email = email;
        this.nationalIdentity = nationalIdentity;
        this.clientType = clientType;
        this.addresses = addresses;
        this.telephones = telephones;
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

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public Set<String> getTelephones() {
        return telephones;
    }

    public void setTelephones(Set<String> telephones) {
        this.telephones = telephones;
    }
}
