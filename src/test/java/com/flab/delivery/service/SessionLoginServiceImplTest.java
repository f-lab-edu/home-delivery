package com.flab.delivery.service;

import com.flab.delivery.utils.SessionConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionLoginServiceImplTest {

    @Mock
    HttpSession httpSession;

    @InjectMocks
    SessionLoginServiceImpl loginService;

    private final String SESSION_ID = SessionConstants.SESSION_ID;

    @Test
    @DisplayName("현재 세션아이디")
    void getSessionUserId() {
        // given
        loginService.loginUser("user1");
        when(httpSession.getAttribute(SESSION_ID)).thenReturn("user1");
        // when
        String sessionUserId = loginService.getSessionUserId();
        // then
        Assertions.assertEquals("user1", sessionUserId);
    }

    @Test
    @DisplayName("로그아웃")
    void logout() {
        // given
        loginService.loginUser("user1");
        when(loginService.getSessionUserId()).thenReturn("user1");
        String sessionId = loginService.getSessionUserId();
        when(loginService.getSessionUserId()).thenReturn(null);
        // when
        loginService.logoutUser();
        String afterSessionId = loginService.getSessionUserId();
        // then
        Assertions.assertNotEquals(sessionId, afterSessionId);
    }


}