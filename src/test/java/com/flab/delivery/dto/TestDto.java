package com.flab.delivery.dto;

public class TestDto {


    public static SignUpDto getSignUpDto() {
        return SignUpDto.builder()
                .id("test")
                .name("테스트")
                .phoneNumber("01011111111")
                .password("!Asdasd1234v")
                .email("test@mail.com")
                .build();
    }
}
