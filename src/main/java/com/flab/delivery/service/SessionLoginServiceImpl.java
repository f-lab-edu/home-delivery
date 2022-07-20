package com.flab.delivery.service;


import com.flab.delivery.utils.SessionConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;


@Slf4j
@Service
@RequiredArgsConstructor
public class SessionLoginServiceImpl implements LoginService {

    private final HttpSession httpSession;

    @Override
    public void loginUser(final String id) {
        httpSession.setAttribute(SessionConstants.SESSION_ID, id);
    }

    @Override
    public String getSessionUserId() {
        return (String) httpSession.getAttribute(SessionConstants.SESSION_ID);
    }

    @Override
    public void logoutUser() {
        httpSession.removeAttribute(SessionConstants.SESSION_ID);
    }
}
