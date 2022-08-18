package com.flab.delivery.dto.order.user;

import com.flab.delivery.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderDto {

    private Long id;
    private Long storeId;
    private int totalPrice;
    private OrderStatus orderStatus;
    private String deliveryAddress;
    private OrderHistoryDto orderHistoryDto;


    public static OrderDto from(OrderRequestDto orderRequestDto, String deliveryAddress) {
        OrderHistoryDto historyDto = OrderHistoryDto.from(orderRequestDto);

        return OrderDto.builder()
                .storeId(orderRequestDto.getStoreId())
                .orderStatus(OrderStatus.BEFORE_PAYMENT)
                .orderHistoryDto(historyDto)
                .totalPrice(historyDto.calculateTotalPrice())
                .deliveryAddress(deliveryAddress)
                .build();
    }
}
