package com.flab.delivery.dto.order.owner;

import com.flab.delivery.dto.order.OrderHistoryDto;
import com.flab.delivery.enums.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OwnerOrderResponseDto {

    private String storeName;
    private OrderStatus status;
    private String userPhoneNumber;
    private String deliveryAddress;
    private LocalDateTime createdAt;
    private OrderHistoryDto history;

}
