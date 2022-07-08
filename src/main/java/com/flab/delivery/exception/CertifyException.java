package com.flab.delivery.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CertifyException extends RuntimeException {

    HttpStatus status;
    public CertifyException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
