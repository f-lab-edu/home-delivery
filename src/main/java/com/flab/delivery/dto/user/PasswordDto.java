package com.flab.delivery.dto.user;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class PasswordDto {

    @NotBlank(message = "비밀번호는 필수 입력값입니다")
    private String password;

    @NotBlank(message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요")
    private String newPassword;

}
