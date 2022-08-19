package com.flab.delivery.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.flab.delivery.dto.order.user.OrderHistoryDto;
import com.flab.delivery.dto.order.user.OrderRequestDto;
import com.flab.delivery.enums.OrderStatus;
import com.flab.delivery.enums.PayType;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto {

    private Long id;
    private Long storeId;
    private Integer orderPrice;
    private OrderStatus status;
    private String deliveryAddress;
    private OrderHistoryDto history;
    private PayType payType;
    private LocalDateTime createdAt;


    public static OrderDto from(OrderRequestDto orderRequestDto, String deliveryAddress) {
        OrderHistoryDto historyDto = OrderHistoryDto.from(orderRequestDto);

        return OrderDto.builder()
                .storeId(orderRequestDto.getStoreId())
                .status(OrderStatus.BEFORE_PAYMENT)
                .history(historyDto)
                .orderPrice(historyDto.calculateTotalPrice())
                .deliveryAddress(deliveryAddress)
                .payType(orderRequestDto.getPayType())
                .build();
    }
}
