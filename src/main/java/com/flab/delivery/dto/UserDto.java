package com.flab.delivery.dto;

import com.flab.delivery.enums.UserLevel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
    public UserDto(String id, String email, String password, String name, String phoneNumber, UserLevel level) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.level = level;
    }
}
