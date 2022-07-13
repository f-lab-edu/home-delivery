package com.flab.delivery.service;

import com.flab.delivery.dto.LoginDto;
import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.TestDto;
import com.flab.delivery.dto.UserDto;
import com.flab.delivery.exception.AuthorizationException;
import com.flab.delivery.exception.UserException;
import com.flab.delivery.mapper.UserMapper;
import com.flab.delivery.utils.PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    UserMapper mapper;

    @Mock
    LoginService loginService;


    @Test
    void signUp_성공() {
        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();
        given(passwordEncoder.encoder(eq(signUpDto.getPassword()))).willReturn(any());

        // when
        userService.signUp(signUpDto);

        //then
        verify(mapper).save(any());
        verify(passwordEncoder).encoder(any());

    }

    @Test
    void checkIdDuplicated_중복된_아이디_없어서_성공() {
        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();
        given(mapper.existsUserById(eq(signUpDto.getId()))).willReturn(false);

        // when
        userService.checkIdDuplicated(signUpDto.getId());

        //then
        verify(mapper).existsUserById(eq(signUpDto.getId()));
    }

    @Test
    void checkIdDuplicated_중복된_아이디_때문에_실패() {
        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();
        given(mapper.existsUserById(eq(signUpDto.getId()))).willReturn(true);

        // when
        assertThatThrownBy(() -> userService.checkIdDuplicated(signUpDto.getId())).isInstanceOf(UserException.class);

        //then
        verify(mapper).existsUserById(eq(signUpDto.getId()));
    }

    @Test
    void login_존재하지_않는_회원_실패() {
        // given
        LoginDto loginDto = TestDto.getLoginDto();
        given(mapper.findUserById(eq(loginDto.getId()))).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> userService.login(loginDto)).isInstanceOf(UserException.class);

        //then
        verify(mapper).findUserById(eq(loginDto.getId()));
        verify(passwordEncoder, never()).isMatch(any(), any());
    }

    @Test
    void login_비밀번호가_일치하지_않아_실패() {
        // given
        LoginDto loginDto = TestDto.getLoginDto();
        UserDto userDto = TestDto.getUserDto();
        given(mapper.findUserById(eq(loginDto.getId()))).willReturn(Optional.of(userDto));
        given(passwordEncoder.isMatch(eq(loginDto.getPassword()), eq(userDto.getPassword()))).willReturn(false);

        // when
        assertThatThrownBy(() -> userService.login(loginDto)).isInstanceOf(AuthorizationException.class);

        //then
        verify(mapper).findUserById(eq(loginDto.getId()));
        verify(passwordEncoder).isMatch(any(), any());
    }

    @Test
    void login_성공() {
        // given
        LoginDto loginDto = TestDto.getLoginDto();
        UserDto userDto = TestDto.getUserDto();
        given(mapper.findUserById(eq(loginDto.getId()))).willReturn(Optional.of(userDto));
        given(passwordEncoder.isMatch(eq(loginDto.getPassword()), eq(userDto.getPassword()))).willReturn(true);

        // when
        userService.login(loginDto);

        //then
        verify(mapper).findUserById(eq(loginDto.getId()));
        verify(passwordEncoder).isMatch(any(), any());
        verify(loginService).login(any());

    }

    @Test
    void logout_성공() {
        // given
        // when
        userService.logout(any());

        //then
        verify(loginService, times(1)).logout(any());
    }
}