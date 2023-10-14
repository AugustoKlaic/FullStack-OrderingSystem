package com.augusto.backend.service.exception;

public class IllegalObjectException extends RuntimeException {

    public IllegalObjectException(String msg) {
        super(msg);
    }

    public IllegalObjectException(String message, Throwable cause) {
        super(message, cause);
    }

}
