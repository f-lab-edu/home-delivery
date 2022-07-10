package com.flab.delivery.service;

import com.flab.delivery.dto.UserDto;
import com.flab.delivery.dto.UserDto.LoginUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionLoginService implements LoginService {

    private static final String SESSION_ID = "SESSION_ID";
    private final HttpSession session;

    @Override
    public void login(UserDto userDto) {
        LoginUserDto loginUserDto = LoginUserDto
                .builder()
                .id(userDto.getId())
                .level(userDto.getLevel())
                .build();
        session.setAttribute(SESSION_ID, loginUserDto);
    }

    @Override
    public void logout() {
        session.removeAttribute(SESSION_ID);
    }

    @Override
    public LoginUserDto getCurrentUser() {
        return (LoginUserDto) session.getAttribute(SESSION_ID);
    }
}
