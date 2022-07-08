package com.flab.delivery.dto;

import com.flab.delivery.enums.UserLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class UserDto {

    @NotBlank
    private String id;

    @NotBlank
    @Email(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$")
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    @Pattern(regexp = "^\\\\d{3}-\\\\d{3,4}-\\\\d{4}$")
    private String phoneNumber;

    @NotBlank
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
