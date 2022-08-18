package com.flab.delivery.dto.order.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.flab.delivery.enums.OrderStatus;
import com.flab.delivery.enums.PayType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetailResponseDto {

    private Integer orderPrice;
    private PayType payType;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private String deliveryAddress;
    private OrderHistoryDto history;
}

