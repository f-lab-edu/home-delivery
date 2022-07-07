package com.flab.delivery.service;

import com.flab.delivery.dto.LoginDto;
import com.flab.delivery.dto.MemberDto;
import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.TestDto;
import com.flab.delivery.exception.MemberException;
import com.flab.delivery.exception.PasswordException;
import com.flab.delivery.mapper.CommonMapper;
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
class CommonServiceTest {

    @InjectMocks
    CommonService commonService;

    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    CommonMapper mapper;

    @Mock
    LoginService loginService;


    @Test
    void signUp_성공() {
        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();
        given(passwordEncoder.encoder(eq(signUpDto.getPassword()))).willReturn(any());

        // when
        commonService.signUp(mapper, signUpDto);

        //then
        verify(mapper).save(any());
        verify(passwordEncoder).encoder(any());

    }

    @Test
    void checkIdDuplicated_중복된_아이디_없어서_성공() {
        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();
        given(mapper.existsById(eq(signUpDto.getId()))).willReturn(false);

        // when
        commonService.checkIdDuplicated(mapper, signUpDto.getId());

        //then
        verify(mapper).existsById(eq(signUpDto.getId()));
    }

    @Test
    void checkIdDuplicated_중복된_아이디_때문에_실패() {
        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();
        given(mapper.existsById(eq(signUpDto.getId()))).willReturn(true);

        // when
        assertThatThrownBy(() -> commonService.checkIdDuplicated(mapper, signUpDto.getId())).isInstanceOf(MemberException.class);

        //then
        verify(mapper).existsById(eq(signUpDto.getId()));
    }

    @Test
    void login_존재하지_않는_회원_실패() {
        // given
        LoginDto loginDto = TestDto.getLoginDto();
        given(mapper.findMemberById(eq(loginDto.getId()))).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> commonService.login(mapper, loginDto)).isInstanceOf(MemberException.class);

        //then
        verify(mapper).findMemberById(eq(loginDto.getId()));
        verify(passwordEncoder, never()).isMatch(any(), any());
    }

    @Test
    void login_비밀번호가_일치하지_않아_실패() {
        // given
        LoginDto loginDto = TestDto.getLoginDto();
        MemberDto memberDto = TestDto.getMemberDto();
        given(mapper.findMemberById(eq(loginDto.getId()))).willReturn(Optional.of(memberDto));
        given(passwordEncoder.isMatch(eq(loginDto.getPassword()), eq(memberDto.getPassword()))).willReturn(false);

        // when
        assertThatThrownBy(() -> commonService.login(mapper, loginDto)).isInstanceOf(PasswordException.class);

        //then
        verify(mapper).findMemberById(eq(loginDto.getId()));
        verify(passwordEncoder).isMatch(any(), any());
    }

    @Test
    void login_성공() {
        // given
        LoginDto loginDto = TestDto.getLoginDto();
        MemberDto memberDto = TestDto.getMemberDto();
        given(mapper.findMemberById(eq(loginDto.getId()))).willReturn(Optional.of(memberDto));
        given(passwordEncoder.isMatch(eq(loginDto.getPassword()), eq(memberDto.getPassword()))).willReturn(true);

        // when
        commonService.login(mapper, loginDto);

        //then
        verify(mapper).findMemberById(eq(loginDto.getId()));
        verify(passwordEncoder).isMatch(any(), any());
        verify(loginService).login(eq(loginDto.getId()));

    }

    @Test
    void logout_성공() {
        // given
        String loginUser = "test";
        given(loginService.getCurrentUserId()).willReturn(loginUser);

        // when
        commonService.logout();

        //then
        verify(loginService, times(1)).getCurrentUserId();
        verify(loginService, times(1)).logout();
    }

    @Test
    void logout_로그인하지_않은_회원_실패() {
        // given
        given(loginService.getCurrentUserId()).willReturn(null);

        // when
        assertThatThrownBy(() -> commonService.logout()).isInstanceOf(MemberException.class);

        //then
        verify(loginService, times(1)).getCurrentUserId();
        verify(loginService, never()).logout();
    }
}