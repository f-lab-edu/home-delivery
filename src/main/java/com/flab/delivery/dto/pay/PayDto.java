package com.flab.delivery.dto.pay;

import com.flab.delivery.enums.PayStatus;
import com.flab.delivery.enums.PayType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PayDto {

    private long orderId;
    private PayStatus status;
    private PayType type;

    public static PayDto completePay(Long orderId, PayType payType) {
        return PayDto.builder()
                .orderId(orderId)
                .type(payType)
                .status(PayStatus.COMPLETE)
                .build();
    }
}
