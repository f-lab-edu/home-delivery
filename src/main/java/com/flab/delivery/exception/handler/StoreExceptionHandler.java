package com.flab.delivery.exception.handler;

import com.flab.delivery.controller.MenuController;
import com.flab.delivery.controller.StoreController;
import com.flab.delivery.exception.StoreException;
import com.flab.delivery.response.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice(assignableTypes = {StoreController.class, MenuController.class})
@Slf4j
public class StoreExceptionHandler {

    @ExceptionHandler(StoreException.class)
    public CommonResult<Void> handleStoreException(StoreException ex, HttpServletRequest request) {
        log.info("Http Method : {}  URI : {}, msg : {}", request.getMethod(), request.getRequestURI(), ex.getMessage());
        return CommonResult.getSimpleResult(ex.getHttpStatus().value(), ex.getMessage());
    }

}
