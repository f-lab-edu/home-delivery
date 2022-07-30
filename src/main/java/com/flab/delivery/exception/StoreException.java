package com.flab.delivery.exception;

import org.springframework.http.HttpStatus;

public class StoreException extends RuntimeException {

    HttpStatus httpStatus;

    public StoreException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

}
