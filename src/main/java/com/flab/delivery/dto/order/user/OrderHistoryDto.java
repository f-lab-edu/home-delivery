package com.flab.delivery.dto.order.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

import static com.flab.delivery.dto.order.user.OrderMenuHistoryDto.createHistory;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderHistoryDto {

    private int menuCount;
    private List<OrderMenuHistoryDto> menuList;


    public static OrderHistoryDto from(OrderRequestDto orderRequestDto) {

        List<OrderMenuHistoryDto> menuHistory = createHistory(orderRequestDto.getMenuList());
        return OrderHistoryDto.builder()
                .menuList(menuHistory)
                .menuCount(menuHistory.size())
                .build();
    }

    public int calculateTotalPrice() {
        int price = 0;

        for (OrderMenuHistoryDto menuHistoryDto : menuList) {
            price += menuHistoryDto.calculateTotalPrice();
        }

        return price;
    }

}

