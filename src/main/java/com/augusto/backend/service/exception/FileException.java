package com.augusto.backend.service.exception;

public class FileException extends RuntimeException {

    public FileException(String msg) {
        super(msg);
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }
}
