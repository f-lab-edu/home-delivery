package com.flab.delivery.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class LoginDto {

    @NotBlank(message = "아이디를 입력해 주세요.")
    private String id;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String password;

}
