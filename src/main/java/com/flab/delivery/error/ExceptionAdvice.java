package com.flab.delivery.error;

import com.flab.delivery.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> methodArgumentNotValidExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException e) {
        log.error("requestUrl : {} , errorCode : {}", request.getRequestURI(), e);
        return getBadResponse(e.getMessage());
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity userExceptionHandler(HttpServletRequest request, UserException e) {
        log.error("requestUrl : {} , errorCode : {}", request.getRequestURI(), e);
        return getBadResponse(e.getMessage());
    }

    private ResponseEntity getBadResponse(String e) {
        return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
    }
}
