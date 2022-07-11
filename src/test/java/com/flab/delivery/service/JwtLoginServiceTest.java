package com.flab.delivery.service;

import com.flab.delivery.dao.TokenDao;
import com.flab.delivery.dto.TestDto;
import com.flab.delivery.dto.TokenDto;
import com.flab.delivery.dto.UserDto;
import com.flab.delivery.security.jwt.JwtProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JwtLoginServiceTest {

    @InjectMocks
    JwtLoginService loginService;

    @Mock
    TokenDao tokenDao;

    @Mock
    JwtProvider jwtProvider;


    @Test
    void login_확인() {
        // given
        UserDto userDto = TestDto.getUserDto();
        TokenDto tokenDto = TestDto.getTokenDto();
        given(jwtProvider.createToken(any())).willReturn(tokenDto);

        // when
        loginService.login(userDto);

        //then
        verify(jwtProvider).createToken(any());
        verify(tokenDao).save(eq(userDto.getId()), eq(tokenDto));
    }

    @Test
    void logout_확인() {
        // given
        UserDto userDto = TestDto.getUserDto();

        // when
        loginService.logout(userDto.getId());

        //then
        verify(tokenDao).remove(eq(userDto.getId()));

    }

}