package com.flab.delivery.dto.order;

import lombok.*;

import java.util.List;

import static com.flab.delivery.dto.order.OrderMenuHistoryDto.createHistory;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class OrderHistoryDto {

    private Long storeId;
    private int menuCount;
    private List<OrderMenuHistoryDto> menuList;


    public static OrderHistoryDto from(OrderRequestDto orderRequestDto) {

        return OrderHistoryDto.builder()
                .menuCount(orderRequestDto.getMenuList().size())
                .storeId(orderRequestDto.getStoreId())
                .menuList(createHistory(orderRequestDto.getMenuList()))
                .build();
    }
}
