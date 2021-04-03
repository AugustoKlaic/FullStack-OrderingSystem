package com.augusto.backend.resource.exception;

import org.springframework.http.HttpStatus;

public class WebException extends Exception {

    private HttpStatus httpStatus;
    private String message;

    public WebException() {
    }

    public WebException(HttpStatus httpStatus, String message) {
        this();
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
