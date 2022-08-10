package com.flab.delivery.exception;

import org.springframework.http.HttpStatus;

public abstract class AbstractException extends RuntimeException {
    HttpStatus httpStatus;

    public AbstractException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}
