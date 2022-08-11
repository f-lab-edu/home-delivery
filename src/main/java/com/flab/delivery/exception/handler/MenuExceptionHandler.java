package com.flab.delivery.exception.handler;

import com.flab.delivery.controller.MenuController;
import com.flab.delivery.exception.MenuException;
import com.flab.delivery.response.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice(assignableTypes = MenuController.class)
public class MenuExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public CommonResult<Void> handleSqlIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex,
                                                                             HttpServletRequest request) {
        log.info("Http Method : {}  URI : {}, msg : {}", request.getMethod(), request.getRequestURI(), ex.getMessage());
        return CommonResult.getSimpleResult(HttpStatus.BAD_REQUEST.value(), "menuGroupId가 존재하지않아 무결성 제약 조건에 위배됩니다");
    }

    @ExceptionHandler(MenuException.class)
    public CommonResult<Void> handleMenuException(MenuException ex, HttpServletRequest request) {
        log.info("Http Method : {}  URI : {}, msg : {}", request.getMethod(), request.getRequestURI(), ex.getMessage());
        return CommonResult.getSimpleResult(ex.getHttpStatus().value(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String msg = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.info("Http Method : {}, URI : {}, msg : {}", request.getMethod(), request.getRequestURI(), msg);
        return CommonResult.getSimpleResult(HttpStatus.BAD_REQUEST.value(), msg);
    }
}
