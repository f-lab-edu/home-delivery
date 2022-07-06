package com.flab.delivery.dto;

public class TestDto {

    public static UserDto getUserDto() {
        return UserDto.builder()
                .id("test")
                .name("테스트")
                .phoneNumber("01011111111")
                .password("#@$ASD1`23")
                .email("test@mail.com")
                .build();
    }

    public static SignUpDto getSignUpDto() {
        return SignUpDto.builder()
                .id("test")
                .name("테스트")
                .phoneNumber("01011111111")
                .password("#@$ASD1`23")
                .email("test@mail.com")
                .build();
    }
}
