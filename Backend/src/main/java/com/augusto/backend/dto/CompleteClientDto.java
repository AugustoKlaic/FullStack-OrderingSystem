package com.augusto.backend.dto;

import com.augusto.backend.domain.enums.ClientTypeEnum;

import java.util.Set;

public class CompleteClientDto {

    private String name;
    private String email;
    private String nationalIdentity;
    private ClientTypeEnum clientType;
    private AddressDto address;
    private Set<String> telephones;

    public CompleteClientDto() {
    }

    public CompleteClientDto(String name, String email, String nationalIdentity,
                             ClientTypeEnum clientType, AddressDto address, Set<String> telephones) {
        this();
        this.name = name;
        this.email = email;
        this.nationalIdentity = nationalIdentity;
        this.clientType = clientType;
        this.address = address;
        this.telephones = telephones;
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

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }

    public Set<String> getTelephones() {
        return telephones;
    }

    public void setTelephones(Set<String> telephones) {
        this.telephones = telephones;
    }
}
