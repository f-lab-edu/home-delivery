package com.flab.delivery.fixture;

import com.flab.delivery.dto.user.UserDto;
import com.flab.delivery.dto.user.UserInfoUpdateDto;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.utils.PasswordEncoder;

import java.time.LocalDateTime;

public class TestDto {

    public static UserDto getUserDto() {
        return UserDto.builder()
                .id("user1")
                .password(PasswordEncoder.encrypt("1111"))
                .type(UserType.USER)
                .email("user1@email.com")
                .phoneNumber("010-1111-1111")
                .name("유저1")
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }

    public static UserInfoUpdateDto getUserInfoUpdateDto() {
        return UserInfoUpdateDto.builder()
                .id("user1")
                .name("테스트2")
                .phoneNumber("010-1234-1234")
                .email("test@naver.com")
                .build();
    }
}
