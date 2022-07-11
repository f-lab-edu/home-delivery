package com.flab.delivery.dto;

import com.flab.delivery.dto.UserDto.AuthDto;

public class TestDto {


    public static final String USER_ID = "test";

    public static SignUpDto getSignUpDto() {
        return SignUpDto.builder()
                .id(USER_ID)
                .name("테스트")
                .phoneNumber("01011111111")
                .password("!Asdasd1234v")
                .email("test@mail.com")
                .level("USER")
                .build();
    }

    public static UserDto getUserDto() {
        return UserDto.builder()
                .id(USER_ID)
                .name("테스트")
                .password("$#$FDFSASD!")
                .email("test@mail.com")
                .phoneNumber("01011111111")
                .level("USER")
                .build();
    }

    public static LoginDto getLoginDto() {
        return new LoginDto(USER_ID, "!Asdasd1234v");
    }

    public static TokenDto getTokenDto() {
        return TokenDto.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
    }

    public static AuthDto getAuthDto() {
        return AuthDto.builder()
                .id(USER_ID)
                .level("USER")
                .build();
    }
}
