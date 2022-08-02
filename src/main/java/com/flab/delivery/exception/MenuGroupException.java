package com.flab.delivery.exception;

import org.springframework.http.HttpStatus;

public class MenuGroupException extends RuntimeException {

    HttpStatus httpStatus;

    public MenuGroupException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}
