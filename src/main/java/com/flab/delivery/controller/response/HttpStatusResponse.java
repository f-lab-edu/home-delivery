package com.flab.delivery.controller.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HttpStatusResponse {

    public static final ResponseEntity<HttpStatus>STATUS_OK = ResponseEntity.ok().build();

    private HttpStatusResponse() {
    }
}
