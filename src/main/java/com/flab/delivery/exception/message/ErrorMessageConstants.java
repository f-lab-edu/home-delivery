package com.flab.delivery.exception.message;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorMessageConstants {

    public static final String BAD_INPUT_MESSAGE = "잘못된 요청 입니다.";

    public static final String UNAUTHORIZED_MESSAGE = "세션 아이디가 존재하지 않습니다";
    public static final String FORBIDDEN_MESSAGE = "권한이 없습니다.";
    
}
