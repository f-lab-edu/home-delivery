package com.flab.delivery.exception.handler;


import com.flab.delivery.controller.UserController;
import com.flab.delivery.exception.LoginException;
import com.flab.delivery.exception.SignUpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice(assignableTypes = {UserController.class})
@Slf4j
public class UserExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String msg = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.info("Http Method : {}, URI : {}, msg : {}", request.getMethod(), request.getRequestURI(), msg);
        return getBadResponse(msg);
    }

    @ExceptionHandler(SignUpException.class)
    public ResponseEntity<String> handleSignUpException(SignUpException ex, HttpServletRequest request) {
        log.info("Http Method : {}  URI : {}, msg : {}", request.getMethod(), request.getRequestURI(), ex.getMessage());
        return getBadResponse(ex.getMessage());
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<String> handleLoginException(LoginException ex, HttpServletRequest request) {
        log.info("Http Method : {}  URI : {}, msg : {}", request.getMethod(), request.getRequestURI(), ex.getMessage());
        return getBadResponse(ex.getMessage());
    }

    private ResponseEntity<String> getBadResponse(String msg) {
        return ResponseEntity.badRequest().body(msg);
    }
}
