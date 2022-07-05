package com.flab.delivery.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class SignUpDto {

    @NotNull
    private String id;

    @NotNull
    @Pattern(regexp = " \"^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$")
    private String password;

    @NotNull
    @Email
    private String email;


    @NotNull
    private String name;

    @NotNull
    private String phoneNumber;

    public UserDto toUserDto() {
        return UserDto.builder()
                .id(id)
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .name(name)
                .build();
    }
}
