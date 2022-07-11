package com.flab.delivery.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenDto {

    @NonNull
    private String accessToken;

    @NonNull
    private String refreshToken;

    @Builder
    public TokenDto(@NonNull String accessToken, @NonNull String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
