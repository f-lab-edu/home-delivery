package com.flab.delivery.controller.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HttpStatusResponse {

    public static final ResponseEntity<HttpStatus> STATUS_OK = ResponseEntity.status(HttpStatus.OK).build();
    public static final ResponseEntity<HttpStatus> STATUS_CREATED = ResponseEntity.status(HttpStatus.CREATED).build();
    public static final ResponseEntity<HttpStatus> STATUS_BAD_REQUEST = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    private HttpStatusResponse() {
    }
}
