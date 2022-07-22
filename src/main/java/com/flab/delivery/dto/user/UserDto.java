package com.flab.delivery.dto.user;

import com.flab.delivery.enums.UserType;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UserDto {

    private String id;

    private String email;

    private String password;

    private String name;

    private String phoneNumber;

    private UserType type;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
