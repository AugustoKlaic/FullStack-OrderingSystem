package com.augusto.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class ForgotMyPasswordDto {

    @NotEmpty(message = "email cannot be empty")
    @Email(message = "invalid email")
    private String email;

    public ForgotMyPasswordDto() {
    }

    public ForgotMyPasswordDto(String email) {
        this();
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
