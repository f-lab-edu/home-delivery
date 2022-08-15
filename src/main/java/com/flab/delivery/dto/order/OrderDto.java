package com.flab.delivery.dto.order;

import com.flab.delivery.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class OrderDto {

    private long id;
    private int deliveryPrice;
    private int totalPrice;
    private OrderStatus orderStatus;
    private List<OrderHistoryDto> orderHistoryList;


    public static OrderDto from(OrderRequestDto orderRequestDto) {
        return OrderDto.builder()
                .deliveryPrice(orderRequestDto.getDeliveryPrice())
                .totalPrice(orderRequestDto.getTotalPrice())
                .orderStatus(OrderStatus.BEFORE_PAYMENT)
                .orderHistoryList(OrderHistoryDto.createHistory(orderRequestDto.getMenuList()))
                .build();
    }
}
