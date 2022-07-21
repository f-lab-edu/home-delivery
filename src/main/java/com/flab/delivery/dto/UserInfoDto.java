package com.flab.delivery.dto;

import com.flab.delivery.enums.UserType;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class UserInfoDto {

    @NonNull
    private String email;

    @NonNull
    private String name;

    @NonNull
    private String phoneNumber;

    @NonNull
    private UserType type;

    @NonNull
    private LocalDateTime createdAt;

    @NonNull
    private LocalDateTime modifiedAt;

    public static UserInfoDto fromUserDto(UserDto userDto) {
        return UserInfoDto.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .phoneNumber(userDto.getPhoneNumber())
                .type(userDto.getType())
                .createdAt(userDto.getCreatedAt())
                .modifiedAt(userDto.getModifiedAt())
                .build();
    }
}
