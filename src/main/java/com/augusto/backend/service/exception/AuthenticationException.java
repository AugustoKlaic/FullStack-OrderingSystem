package com.augusto.backend.service.exception;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String msg) {
        super(msg);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
