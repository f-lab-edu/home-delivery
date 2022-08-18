package com.flab.delivery.dto.order.user;


import com.flab.delivery.enums.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class OrderSimpleResponseDto {

    private Long storeId;
    private int menuCount;
    private String menuName;
    private OrderStatus status;
    private int orderPrice;
    private LocalDateTime createdAt;

    public String getMenuName() {
        return menuName.substring(1, menuName.length() - 1);
    }
}
