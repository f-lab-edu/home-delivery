package com.flab.delivery.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserDto {
    @NonNull
    private String id;

    @NonNull
    private String name;

    @NonNull
    private String password;

    @NonNull
    private String email;

    @NonNull
    private String phoneNumber;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
