package com.augusto.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class ClientDto {

    @NotEmpty(message = "name cannot be empty")
    private String name;

    @NotEmpty(message = "email cannot be empty")
    @Email(message = "invalid email")
    private String email;

    public ClientDto() {
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
}
