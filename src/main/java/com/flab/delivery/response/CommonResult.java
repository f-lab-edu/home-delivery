package com.flab.delivery.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommonResult<T> {

    private static final String version = "v1";
    private static final String SUCCESS_MESSAGE = "요청 성공하였습니다.";
    private int status;
    private String message;
    private T data;

    /**
     * 단순 성공 응답
     */
    public static CommonResult getSimpleSuccessResult(int status) {
        return CommonResult.builder()
                .status(status)
                .message(SUCCESS_MESSAGE)
                .build();
    }

    /**
     * 단순 실패 응답
     */
    public static CommonResult getSimpleFailResult(int status) {
        return CommonResult.builder()
                .status(status)
                .message("요청 실패하였습니다.")
                .build();
    }

    /**
     * 단순 응답
     */
    public static CommonResult getSimpleResult(int status, String message) {
        return CommonResult.builder()
                .status(status)
                .message(message)
                .build();
    }

    /**
     * 데이터를 포함하는 응답
     */
    public static <T> CommonResult getDataResult(int status, String message, T data) {
        return CommonResult.builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * 데이터를 포함하는 단순 성공 응답
     */
    public static <T> CommonResult getDataSuccessResult(T data) {
        return CommonResult.builder()
                .status(200)
                .message(SUCCESS_MESSAGE)
                .data(data)
                .build();
    }
}
