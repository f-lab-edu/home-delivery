package com.flab.delivery.exception;

import org.springframework.http.HttpStatus;

public class SessionLoginException extends RuntimeException {

    HttpStatus httpStatus;
    public SessionLoginException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus(){
        return this.httpStatus;
    }
}
