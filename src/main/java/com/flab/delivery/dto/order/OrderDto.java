package com.flab.delivery.dto.order;

import com.flab.delivery.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class OrderDto {

    private Long id;
    private int totalPrice;
    private OrderStatus orderStatus;
    private String deliveryAddress;
    private OrderHistoryDto orderHistoryDto;


    public static OrderDto from(OrderRequestDto orderRequestDto, String deliveryAddress) {
        OrderHistoryDto historyDto = OrderHistoryDto.from(orderRequestDto);

        return OrderDto.builder()
                .orderStatus(OrderStatus.BEFORE_PAYMENT)
                .orderHistoryDto(historyDto)
                .totalPrice(historyDto.getTotalPrice())
                .deliveryAddress(deliveryAddress)
                .build();
    }
}
