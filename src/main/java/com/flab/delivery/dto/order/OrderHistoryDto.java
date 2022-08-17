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

        List<OrderMenuHistoryDto> menuHistory = createHistory(orderRequestDto.getMenuList());
        return OrderHistoryDto.builder()
                .storeId(orderRequestDto.getStoreId())
                .menuList(menuHistory)
                .menuCount(menuHistory.size())
                .build();
    }

    public int getTotalPrice() {
        int price = 0;

        for (OrderMenuHistoryDto menuHistoryDto : menuList) {
            price += menuHistoryDto.getTotalPrice();
        }

        return price;
    }

}
