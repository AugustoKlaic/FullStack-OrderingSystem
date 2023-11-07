package com.augusto.backend.dto;

import com.augusto.backend.domain.enums.ClientTypeEnum;
import com.augusto.backend.resource.validator.ClientCreate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

@ClientCreate
public class CompleteClientDto {

    @NotEmpty(message = "name cannot be empty")
    private String name;

    @NotEmpty(message = "email cannot be empty")
    @Email(message = "invalid email")
    private String email;

    @NotEmpty(message = "password cannot be empty")
    private String clientPassword;

    private String nationalIdentity;
    private ClientTypeEnum clientType;
    private AddressDto address;
    private Set<String> telephones;

    public CompleteClientDto() {
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

    public String getClientPassword() {
        return clientPassword;
    }

    public void setClientPassword(String clientPassword) {
        this.clientPassword = clientPassword;
    }
}
