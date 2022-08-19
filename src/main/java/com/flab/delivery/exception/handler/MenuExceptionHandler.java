package com.flab.delivery.exception.handler;

import com.flab.delivery.controller.MenuController;
import com.flab.delivery.exception.MenuException;
import com.flab.delivery.response.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice(assignableTypes = MenuController.class)
public class MenuExceptionHandler {

    @ExceptionHandler(MenuException.class)
    public CommonResult<Void> handleMenuException(MenuException ex, HttpServletRequest request) {
        log.info("Http Method : {}  URI : {}, msg : {}", request.getMethod(), request.getRequestURI(), ex.getMessage());
        return CommonResult.getSimpleResult(ex.getHttpStatus().value(), ex.getMessage());
    }

}
