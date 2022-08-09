package com.flab.delivery.exception;

import org.springframework.http.HttpStatus;

public class StoreException extends AbstractException {


    public StoreException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
