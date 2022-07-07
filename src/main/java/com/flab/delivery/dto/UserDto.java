package com.flab.delivery.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDto {

    @NonNull
    private String id;

    @NonNull
    private String password;

    @NonNull
    private String name;

    @NonNull
    private String email;

    @NonNull
    private String phoneNumber;

    @NonNull
    private String level;

    @Builder
    public UserDto(@NonNull String id, @NonNull String password, @NonNull String name, @NonNull String email, @NonNull String phoneNumber, @NonNull String level) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.level = level;
    }
}
