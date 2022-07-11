package com.flab.delivery.service;

import com.flab.delivery.dao.TokenDao;
import com.flab.delivery.dto.TestDto;
import com.flab.delivery.dto.TokenDto;
import com.flab.delivery.dto.UserDto;
import com.flab.delivery.dto.UserDto.AuthDto;
import com.flab.delivery.exception.CertifyException;
import com.flab.delivery.security.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
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
        verify(tokenDao).removeTokenByUserId(eq(userDto.getId()));

    }


    @Test
    void reissue_성공() {
        // given
        TokenDto tokenDto = TestDto.getTokenDto();
        TokenDto newToken = TokenDto.builder().accessToken("newAccessToken").refreshToken("newRefreshToken").build();
        AuthDto authDto = TestDto.getAuthDto();
        String userId = authDto.getId();

        given(jwtProvider.getAuthDto(eq(tokenDto.getRefreshToken()))).willReturn(authDto);
        given(tokenDao.getTokenByUserId(eq(userId))).willReturn(Optional.of(tokenDto));
        given(jwtProvider.createToken(eq(userId), eq("USER"))).willReturn(newToken);
        given(tokenDao.save(eq(userId), eq(newToken))).willReturn(newToken);

        // when
        TokenDto reissueToken = loginService.reissue(tokenDto.getAccessToken(), tokenDto.getRefreshToken());

        //then
        assertThat(reissueToken.getRefreshToken()).isEqualTo(newToken.getRefreshToken());
        assertThat(reissueToken.getAccessToken()).isEqualTo(newToken.getAccessToken());
    }


    @Test
    void reissue_DB에_존재하지_않는_refreshToken_으로_실패() {
        // given
        TokenDto tokenDto = TestDto.getTokenDto();
        AuthDto authDto = TestDto.getAuthDto();
        String userId = authDto.getId();

        given(jwtProvider.getAuthDto(eq(tokenDto.getRefreshToken()))).willReturn(authDto);
        given(tokenDao.getTokenByUserId(eq(userId))).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> loginService.reissue(tokenDto.getAccessToken(), tokenDto.getRefreshToken())).isInstanceOf(CertifyException.class);

        //then
        verify(tokenDao, never()).save(any(), any());
        verify(jwtProvider, never()).createToken(any(), anyString());
    }


    @Test
    void reissue_DB와_동일하지_않은_refreshToken_으로_실패() {

        // given
        TokenDto tokenDto = TestDto.getTokenDto();
        AuthDto authDto = TestDto.getAuthDto();
        String userId = authDto.getId();
        TokenDto wrongToken = TokenDto.builder().accessToken("wrongToken").refreshToken("wrongToken").build();

        given(jwtProvider.getAuthDto(eq(tokenDto.getRefreshToken()))).willReturn(authDto);
        given(tokenDao.getTokenByUserId(eq(userId))).willReturn(Optional.of(wrongToken));

        // when
        assertThatThrownBy(() -> loginService.reissue(tokenDto.getAccessToken(), tokenDto.getRefreshToken())).isInstanceOf(CertifyException.class);

        //then
        verify(tokenDao, never()).save(any(), any());
        verify(jwtProvider, never()).createToken(any(), anyString());
    }
}