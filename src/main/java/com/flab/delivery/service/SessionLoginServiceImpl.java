package com.flab.delivery.service;


import com.flab.delivery.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;


@Slf4j
@Service
@RequiredArgsConstructor
public class SessionLoginServiceImpl implements LoginService {

    private final HttpSession httpSession;
    private final String SESSION_ID = "SESSION_ID";


    @Override
    public void loginUser(final String id) {
        httpSession.setAttribute(SESSION_ID, id);
    }

    @Override
    public String getSessionUserId() {
        return (String)httpSession.getAttribute(SESSION_ID);
    }

    // 세션으로 로그인 한 후 로그아웃을 시켜줘야한다
    @Override
    public void logoutUser() {
        httpSession.removeAttribute(SESSION_ID);
    }
}
