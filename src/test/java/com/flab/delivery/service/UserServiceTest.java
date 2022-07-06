package com.flab.delivery.service;

import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.TestDto;
import com.flab.delivery.exception.UserException;
import com.flab.delivery.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserMapper userMapper;


    @Test
    void signUp_성공() {
        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();
        given(userMapper.existUserById(eq(signUpDto.getId()))).willReturn(false);

        // when
        //then
        userService.signUp(signUpDto);

    }

    @Test
    void signUp_중복된_아이디라_실패() {
        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();
        given(userMapper.existUserById(eq(signUpDto.getId()))).willReturn(true);

        // when
        //then
        assertThatThrownBy(() -> userService.signUp(signUpDto)).isInstanceOf(UserException.class);
    }
}