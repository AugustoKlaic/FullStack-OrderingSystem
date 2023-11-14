package com.augusto.backend.security;

public class CredentialsHelper {

    private String clientId;
    private String token;

    public CredentialsHelper() {
    }

    public CredentialsHelper(String clientId, String token) {
        this();
        this.clientId = clientId;
        this.token = token;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
