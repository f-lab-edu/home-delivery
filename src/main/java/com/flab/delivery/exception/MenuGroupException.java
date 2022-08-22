package com.flab.delivery.exception;

import org.springframework.http.HttpStatus;

public class MenuGroupException extends AbstractException {


    public MenuGroupException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

}
