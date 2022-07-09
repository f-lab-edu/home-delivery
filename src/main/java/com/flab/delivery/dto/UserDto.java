package com.flab.delivery.dto;

import com.flab.delivery.enums.UserLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class UserDto {

    private String id;

    private String email;

    private String password;

    private String name;

    private String phoneNumber;

    private UserLevel level;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    @Builder
    public UserDto(String id, String email, String password, String name, String phoneNumber, UserLevel level){
        this.id = id;
        this. email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.level = level;
    }
}
