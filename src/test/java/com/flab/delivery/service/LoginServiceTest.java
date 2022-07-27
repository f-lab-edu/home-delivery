package com.flab.delivery.service;

import com.flab.delivery.dto.user.UserDto;
import com.flab.delivery.enums.UserType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpSession;

import static com.flab.delivery.utils.SessionConstants.AUTH_TYPE;
import static com.flab.delivery.utils.SessionConstants.SESSION_ID;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    HttpSession httpSession;

    @InjectMocks
    LoginService loginService;


    @Test
    @DisplayName("현재 세션아이디")
    void getSessionUserId() {
        // given
        UserDto userDto = UserDto.builder()
                .id("user1")
                .type(UserType.USER)
                .build();

        loginService.loginUser(userDto);
        when(httpSession.getAttribute(SESSION_ID)).thenReturn("user1");
        when(httpSession.getAttribute(AUTH_TYPE)).thenReturn(UserType.USER);
        // when
        String sessionUserId = loginService.getSessionUserId();
        UserType userType = loginService.getUserType();
        // then
        Assertions.assertEquals("user1", sessionUserId);
        Assertions.assertEquals(UserType.USER, userType);
    }

    @Test
    @DisplayName("로그아웃")
    void logout() {
        // given
        UserDto userDto = UserDto.builder()
                .id("user1")
                .type(UserType.USER)
                .build();

        loginService.loginUser(userDto);
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