package com.flab.delivery.dto.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class UserInfoUpdateDto {

    @NonNull
    private String id;

    @NotNull(message = "이메일은 필수 입력값입니다")
    @Email(message = "이메일 형식이 아닙니다", regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$")
    private String email;

    @NonNull
    private String name;

    @NotNull(message = "핸드폰 번호는 필수 입력값입니다")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "핸드폰 형식이 아닙니다")
    private String phoneNumber;


}