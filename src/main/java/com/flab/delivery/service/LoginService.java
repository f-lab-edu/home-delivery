package com.flab.delivery.service;


import com.flab.delivery.dto.user.UserDto;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.utils.SessionConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;


@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final HttpSession httpSession;

    public void loginUser(UserDto userDto) {
        httpSession.setAttribute(SessionConstants.SESSION_ID, userDto.getId());
        httpSession.setAttribute(SessionConstants.AUTH_TYPE, userDto.getType());
    }

    public String getSessionUserId() {
        return (String) httpSession.getAttribute(SessionConstants.SESSION_ID);
    }

    public UserType getUserType() {
        return (UserType) httpSession.getAttribute(SessionConstants.AUTH_TYPE);
    }

    public void logoutUser() {
        httpSession.removeAttribute(SessionConstants.SESSION_ID);
        httpSession.removeAttribute(SessionConstants.AUTH_TYPE);
    }
}
