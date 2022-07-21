package com.flab.delivery.service;


import com.flab.delivery.dto.UserDto;
import com.flab.delivery.enums.UserType;
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
    public void loginUser(UserDto userDto) {
        httpSession.setAttribute(SessionConstants.SESSION_ID, userDto.getId());
        httpSession.setAttribute(SessionConstants.AUTH_TYPE, userDto.getType());
    }

    @Override
    public String getSessionUserId() {
        return (String) httpSession.getAttribute(SessionConstants.SESSION_ID);
    }

    @Override
    public UserType getUserType() {
        return (UserType) httpSession.getAttribute(SessionConstants.AUTH_TYPE);
    }

    @Override
    public void logoutUser() {
        httpSession.removeAttribute(SessionConstants.SESSION_ID);
        httpSession.removeAttribute(SessionConstants.AUTH_TYPE);
    }
}
