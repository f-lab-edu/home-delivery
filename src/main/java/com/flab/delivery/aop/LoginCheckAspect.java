package com.flab.delivery.aop;


import com.flab.delivery.annotation.LoginCheck;
import com.flab.delivery.dto.UserDto;
import com.flab.delivery.enums.UserLevel;
import com.flab.delivery.exception.LoginException;
import com.flab.delivery.exception.SessionLoginException;
import com.flab.delivery.mapper.UserMapper;
import com.flab.delivery.service.LoginService;
import com.flab.delivery.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LoginCheckAspect {

    private final LoginService loginService;
    private final UserMapper userMapper;

    /**
     * 시큐리티는 인증, 권한체크를 한다
     * 세션이 존재하는지 확인
     * 존재안하면 예외
     * 권한 확인
     */
    @Before("@annotation(com.flab.delivery.annotation.LoginCheck) && @annotation(target)")
    public void sessionLoginCheck(LoginCheck target){
        String sessionUserId = loginService.getSessionUserId();

        if(sessionUserId == null){
            throw new SessionLoginException("세션 아이디가 존재하지 않습니다");
        }

        UserLevel targetUserLevel = target.userLevel();

        if (targetUserLevel == UserLevel.ALL) {
            return;
        }

        UserDto findUser = userMapper.findById(sessionUserId);

        if(findUser.getLevel() != targetUserLevel){
            throw new SessionLoginException("권한이 없습니다");
        }
    }





}
