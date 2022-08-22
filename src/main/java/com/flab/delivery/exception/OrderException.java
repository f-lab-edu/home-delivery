package com.flab.delivery.exception;

import org.springframework.http.HttpStatus;

public class OrderException extends AbstractException {

    public OrderException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
