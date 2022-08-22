package com.flab.delivery.exception;

import org.springframework.http.HttpStatus;

public class OptionException extends AbstractException {

    public OptionException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

}
