package com.flab.delivery.aop;

import com.flab.delivery.annotation.HasAuthorization;
import com.flab.delivery.annotation.HasAuthorization.UserType;
import com.flab.delivery.dto.UserDto;
import com.flab.delivery.exception.AuthException;
import com.flab.delivery.mapper.UserMapper;
import com.flab.delivery.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthAspect {

    private final LoginService loginService;
    private final UserMapper userMapper;


    @Before("@annotation(target)")
    public void checkCertify(HasAuthorization target) {

        String currentUserId = loginService.getCurrentUserId();

        // 인증
        if (currentUserId == null) {
            throw new AuthException("로그인 되지 않은 사용자 입니다.", HttpStatus.UNAUTHORIZED);
        }

        if (target.level() == UserType.ALL) {
            return;
        }

        UserDto user = userMapper.findUserById(currentUserId).get();

        if (UserType.valueOf(user.getLevel()) != target.level()) {
            throw new AuthException("권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
    }
}
