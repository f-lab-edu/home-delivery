package com.flab.delivery.exception.handler;


import com.flab.delivery.controller.AddressController;
import com.flab.delivery.exception.AddressException;
import com.flab.delivery.response.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice(assignableTypes = {AddressController.class})
@Slf4j
public class AddressExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String msg = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.info("Http Method : {}, URI : {}, msg : {}", request.getMethod(), request.getRequestURI(), msg);
        return getBadResponse(msg);
    }

    @ExceptionHandler(AddressException.class)
    public CommonResult<Void> handleAddressException(AddressException ex, HttpServletRequest request) {
        log.info("Http Method : {}  URI : {}, msg : {}", request.getMethod(), request.getRequestURI(), ex.getMessage());
        return getBadResponse(ex.getMessage());
    }

    private CommonResult<Void> getBadResponse(String msg) {
        return CommonResult.getSimpleResult(HttpStatus.BAD_REQUEST.value(), msg);
    }
}
