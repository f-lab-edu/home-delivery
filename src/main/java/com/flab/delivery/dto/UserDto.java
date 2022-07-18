package com.flab.delivery.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
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

}
