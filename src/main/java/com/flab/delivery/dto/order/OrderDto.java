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
    private List<OrderHistoryDto> orderHistoryList;
    private String deliveryAddress;


    public static OrderDto from(OrderRequestDto orderRequestDto, String deliveryAddress) {
        return OrderDto.builder()
                .totalPrice(orderRequestDto.getTotalPrice())
                .orderStatus(OrderStatus.BEFORE_PAYMENT)
                .orderHistoryList(OrderHistoryDto.createHistory(orderRequestDto.getMenuList()))
                .deliveryAddress(deliveryAddress)
                .build();
    }
}
