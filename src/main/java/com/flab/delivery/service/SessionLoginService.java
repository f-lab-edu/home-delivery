package com.flab.delivery.service;

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
    public void login(String id) {
        session.setAttribute(SESSION_ID, id);
    }

    @Override
    public void logout() {
        session.removeAttribute(SESSION_ID);
    }

    @Override
    public String getCurrentUserId() {
        return (String) session.getAttribute(SESSION_ID);
    }
}
