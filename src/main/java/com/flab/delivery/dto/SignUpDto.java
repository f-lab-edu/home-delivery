package com.flab.delivery.dto;

import com.flab.delivery.enums.UserType;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SignUpDto {

    @NotBlank(message = "아이디 길이는 4~20자 입니다")
    @Length(min = 4, max = 20, message = "아이디 길이는 4~20자 입니다")
    private String id;

    @NotNull(message = "이메일은 필수 입력값입니다")
    @Email(message = "이메일 형식이 아닙니다", regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$")
    private String email;

    @NotBlank(message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요")
    private String password;

    @NotBlank(message = "이름은 필수 입력값입니다")
    private String name;

    @NotNull(message = "핸드폰 번호는 필수 입력값입니다")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "핸드폰 형식이 아닙니다")
    private String phoneNumber;

    @NotNull(message = "권한은 필수 입력값입니다")
    private UserType type;
}
