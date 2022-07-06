package com.flab.delivery.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpDto {

    @NotNull
    private String id;

    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$")
    private String password;

    @NotNull
    @Email
    private String email;


    @NotNull
    private String name;

    @NotNull
    private String phoneNumber;

    @Builder
    public SignUpDto(String id, String password, String email, String name, String phoneNumber) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
