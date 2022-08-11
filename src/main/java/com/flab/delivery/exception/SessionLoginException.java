package com.flab.delivery.exception;

import org.springframework.http.HttpStatus;

public class SessionLoginException extends AbstractException {

    public SessionLoginException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
