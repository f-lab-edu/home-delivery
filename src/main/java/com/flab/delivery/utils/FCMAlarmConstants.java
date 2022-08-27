package com.flab.delivery.utils;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FCMAlarmConstants {

    // 유저가 주문하고 메장에서 유저에게 알림보내는것
    public static final String REQUEST_COMPLETE_TITLE = "주문 접수알림";
    public static final String REQUEST_COMPLETE = "주문 접수되었습니다";

    // 주문취소 유저에게 보낼때
    public static final String REQUEST_CANCEL_TITLE = "주문 취소알림";
    public static final String REQUEST_CANCEL = "주문 취소되었습니다";

    // 라이더가 픽업한 경우
    public static final String PICK_UP_TILE = "배달시작 알림";
    public static final String PICK_UP = "주문한 음식이 배달시작되었습니다";

    // 라이더가 배달완료 누른 경우
    public static final String FINISH_DELIVERY_TITLE = "배달완료 알림";
    public static final String FINISH_DELIVERY = "배달완료 되었습니다";



}
