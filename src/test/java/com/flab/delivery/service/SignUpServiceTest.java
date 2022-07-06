package com.flab.delivery.service;

import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.TestDto;
import com.flab.delivery.exception.SignUpException;
import com.flab.delivery.mapper.CommonMapper;
import com.flab.delivery.utils.PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SignUpServiceTest {

    @InjectMocks
    SignUpService signUpService;

    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    CommonMapper mapper;


    @Test
    void signUp_성공() {
        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();
        given(passwordEncoder.encoder(eq(signUpDto.getPassword()))).willReturn(any());

        // when
        signUpService.signUp(mapper, signUpDto);

        //then
        verify(mapper).save(any());
        verify(passwordEncoder).encoder(any());

    }

    @Test
    void checkIdDuplicated_중복된_아이디_없어서_성공() {
        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();
        given(mapper.existById(eq(signUpDto.getId()))).willReturn(false);

        // when
        signUpService.checkIdDuplicated(mapper, signUpDto.getId());

        //then
        verify(mapper).existById(eq(signUpDto.getId()));
    }

    @Test
    void checkIdDuplicated_중복된_아이디_때문에_실패() {
        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();
        given(mapper.existById(eq(signUpDto.getId()))).willReturn(true);

        // when
        assertThatThrownBy(() -> signUpService.checkIdDuplicated(mapper, signUpDto.getId())).isInstanceOf(SignUpException.class);

        //then
        verify(mapper).existById(eq(signUpDto.getId()));
    }
}