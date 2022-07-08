package com.flab.delivery.aop;

import com.flab.delivery.exception.CertifyException;
import com.flab.delivery.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class CertifyAspect {

    private final LoginService loginService;


    @Before("@annotation(com.flab.delivery.annotation.hasCertify)")
    public void checkCertify() {

        String currentUserId = loginService.getCurrentUserId();

        if (currentUserId == null) {
            throw new CertifyException("로그인 되지 않은 사용자 입니다.");
        }
    }

}
