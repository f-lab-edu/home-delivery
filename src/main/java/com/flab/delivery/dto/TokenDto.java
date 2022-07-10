package com.flab.delivery.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class TokenDto {

    @NonNull
    private String accessToken;

    @NonNull
    private String refreshToken;
}
