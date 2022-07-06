package com.flab.delivery.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder
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
}
