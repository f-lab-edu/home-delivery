package com.flab.delivery.security.session;

import com.flab.delivery.dto.UserDto;
import com.flab.delivery.dto.UserDto.LoginUserDto;
import com.flab.delivery.exception.CertifyException;
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
public class CertifyAspect {

    private final LoginService loginService;
    private final UserMapper userMapper;


    @Before("@annotation(target)")
    public void checkCertify(HasCertify target) {

        LoginUserDto currentUser = loginService.getCurrentUser();

        // 인증
        if (currentUser == null) {
            throw new CertifyException("로그인 되지 않은 사용자 입니다.", HttpStatus.UNAUTHORIZED);
        }

        if (target.level() == HasCertify.UserLevel.ALL) {
            return;
        }

        UserDto user = userMapper.findUserById(currentUser.getId()).get();

        if (HasCertify.UserLevel.valueOf(user.getLevel()) != target.level()) {
            throw new CertifyException("권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
    }
}
