package com.augusto.backend.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class ClientDto {

    private Integer id;

    @NotEmpty(message = "name cannot be empty")
    private String name;

    @NotEmpty(message = "email cannot be empty")
    @Email(message = "invalid email")
    private String email;

    public ClientDto() {
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
}
