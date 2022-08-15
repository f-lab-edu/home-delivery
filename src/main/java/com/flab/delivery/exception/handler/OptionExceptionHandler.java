package com.flab.delivery.exception.handler;

import com.flab.delivery.controller.OptionController;
import com.flab.delivery.exception.OptionException;
import com.flab.delivery.response.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice(assignableTypes = OptionController.class)
public class OptionExceptionHandler {

    @ExceptionHandler(OptionException.class)
    public CommonResult<Void> handleOptionException(OptionException ex, HttpServletRequest request) {
        log.info("Http Method : {}  URI : {}, msg : {}", request.getMethod(), request.getRequestURI(), ex.getMessage());
        return CommonResult.getSimpleResult(ex.getHttpStatus().value(), ex.getMessage());
    }



}
