package com.augusto.backend.domain.enums;

import java.util.Arrays;

public enum ClientProfileEnum {

    ADMIN(1, "ROLE_ADMIN"),
    CLIENT(2, "ROLE_CLIENT");

    private Integer code;
    private String description;

    ClientProfileEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ClientProfileEnum getEnum(Integer code) {
        return Arrays.stream(ClientProfileEnum.values())
                .filter(clientProfile -> clientProfile.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid code received"));
    }
}
