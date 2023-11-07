package com.augusto.backend.service.exception;

public class AuthorizationException extends RuntimeException {

    public AuthorizationException(String msg) {
        super(msg);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
