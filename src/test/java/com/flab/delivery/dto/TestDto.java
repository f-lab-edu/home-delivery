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

    public static MemberDto getMemberDto() {
        return MemberDto.builder()
                .id("test")
                .name("테스트")
                .password("$#$FDFSASD!")
                .email("test@mail.com")
                .phoneNumber("01011111111")
                .build();
    }

    public static LoginDto getLoginDto() {
        return new LoginDto("test", "!Asdasd1234v");
    }
}
