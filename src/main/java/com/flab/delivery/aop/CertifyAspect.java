package com.flab.delivery.aop;

import com.flab.delivery.annotation.hasCertify;
import com.flab.delivery.annotation.hasCertify.UserLevel;
import com.flab.delivery.dto.UserDto;
import com.flab.delivery.exception.CertifyException;
import com.flab.delivery.mapper.UserMapper;
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
    private final UserMapper userMapper;


    @Before("@annotation(target)")
    public void checkCertify(hasCertify target) {

        String currentUserId = loginService.getCurrentUserId();

        // 인증
        if (currentUserId == null) {
            throw new CertifyException("로그인 되지 않은 사용자 입니다.");
        }

        if (target.level() == UserLevel.ALL) {
            return;
        }

        UserDto user = userMapper.findUserById(currentUserId).get();

        if (UserLevel.valueOf(user.getLevel()) != target.level()) {
            throw new CertifyException("권한이 없습니다.");
        }
    }
}
