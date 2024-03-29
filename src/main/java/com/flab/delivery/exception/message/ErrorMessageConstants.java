package com.flab.delivery.exception.message;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorMessageConstants {

    public static final String BAD_REQUEST_MESSAGE = "잘못된 요청 입니다.";

    public static final String UNAUTHORIZED_MESSAGE = "세션 아이디가 존재하지 않습니다";
    public static final String FORBIDDEN_MESSAGE = "권한이 없습니다.";

    public static final String NOT_ENOUGH_DELIVERY_REQUEST_TIME_MESSAGE = "이미 배차 요청을 하셨습니다. 잠시 후 다시 시도해주세요";

    public static final String NOT_STAND_BY_RIDER = "해당 지역에 출근중인 라이더가 아닙니다.";
    public static final String CANT_DELIVERY_IN_LOCATION_MESSAGE = "해당 지역에서 배달 가능한 라이더가 아닙니다.";
    public static final String NOT_EXIST_ORDER_REQUEST_MESSAGE = "배달 요청이 존재하지 않습니다.";
}
