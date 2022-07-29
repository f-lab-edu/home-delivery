package com.flab.delivery.aop;


import com.flab.delivery.annotation.LoginCheck;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.exception.SessionLoginException;
import com.flab.delivery.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LoginCheckAspect {

    private final LoginService loginService;

    @Before("@annotation(com.flab.delivery.annotation.LoginCheck) && @annotation(target)")
    public void sessionLoginCheck(LoginCheck target) {
        String sessionUserId = loginService.getSessionUserId();

        if (sessionUserId == null) {
            throw new SessionLoginException("세션 아이디가 존재하지 않습니다", HttpStatus.UNAUTHORIZED);
        }

        UserType targetUserType = target.userType();

        if (targetUserType == UserType.ALL) {
            return;
        }

        UserType userType = loginService.getUserType();

        if (userType != targetUserType) {
            throw new SessionLoginException("권한이 없습니다", HttpStatus.FORBIDDEN);
        }
    }


}
