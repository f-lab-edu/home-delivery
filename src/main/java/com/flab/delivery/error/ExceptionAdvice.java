package com.flab.delivery.error;

import com.flab.delivery.controller.reponse.CommonResult;
import com.flab.delivery.exception.AuthException;
import com.flab.delivery.exception.BadInputException;
import com.flab.delivery.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult methodArgumentNotValidExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException e) {
        log.error("requestUrl : {} , errorCode : {}", request.getRequestURI(), e);
        return getBadResponse(e.getMessage());
    }

    @ExceptionHandler(UserException.class)
    public CommonResult userExceptionHandler(HttpServletRequest request, UserException e) {
        log.error("requestUrl : {} , errorCode : {}", request.getRequestURI(), e);
        return getBadResponse(e.getMessage());
    }

    @ExceptionHandler(BadInputException.class)
    public CommonResult badInputExceptionHandler(HttpServletRequest request, BadInputException e) {
        log.error("requestUrl : {} , errorCode : {}", request.getRequestURI(), e);
        return getBadResponse(e.getMessage());
    }

    @ExceptionHandler(AuthException.class)
    public CommonResult passwordExceptionHandler(AuthException e) {
        return CommonResult.getSimpleResult(e.getStatus().value(), e.getMessage());
    }

    private CommonResult getBadResponse(String errorMessage) {
        return CommonResult.getSimpleResult(HttpStatus.BAD_REQUEST.value(), errorMessage);
    }
}
