package com.flab.delivery.exception;

import org.springframework.http.HttpStatus;

public class MenuException extends AbstractException {

    public MenuException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
