package com.flab.delivery.exception.handler;

import com.flab.delivery.exception.NotFoundException;
import com.flab.delivery.exception.SessionLoginException;
import com.flab.delivery.response.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SessionLoginException.class)
    public CommonResult<Void> handleSessionLoginException(SessionLoginException ex, HttpServletRequest request) {
        log.info("Http Method : {}  URI : {}, msg : {}", request.getMethod(), request.getRequestURI(), ex.getMessage());
        return CommonResult.getSimpleResult(ex.getHttpStatus().value(), ex.getMessage());
    }

    @ExceptionHandler(com.flab.delivery.exception.NotFoundException.class)
    public CommonResult<Void> handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        log.info("Http Method : {}  URI : {}, msg : {}", request.getMethod(), request.getRequestURI(), ex.getMessage());
        return CommonResult.getSimpleResult(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }
}
