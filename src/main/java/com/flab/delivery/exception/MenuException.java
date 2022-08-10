package com.flab.delivery.exception;

import org.springframework.http.HttpStatus;

public class MenuException extends RuntimeException {
    HttpStatus httpStatus;

    public MenuException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}
