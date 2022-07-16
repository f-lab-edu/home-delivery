package com.flab.delivery.dto;

import com.flab.delivery.utils.PasswordEncoder;

public class TestDto {


    public static SignUpDto getSignUpDto() {
        return SignUpDto.builder()
                .id("test")
                .name("테스트")
                .phoneNumber("01011111111")
                .password("!Asdasd1234v")
                .email("test@mail.com")
                .level("USER")
                .build();
    }

    public static UserDto getMemberDto() {
        return UserDto.builder()
                .id("test")
                .name("테스트")
                .password(PasswordEncoder.encode("!Asdasd1234v"))
                .email("test@mail.com")
                .phoneNumber("01011111111")
                .level("USER")
                .build();
    }

    public static LoginDto getLoginDto() {
        return new LoginDto("test", "!Asdasd1234v");
    }
}
