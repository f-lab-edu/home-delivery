package com.flab.delivery.dto.order.rider;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.flab.delivery.enums.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDeliveryDto {

    private Long orderId;
    private Long addressId;
    private String storeName;
    private String storeAddress;
    private String deliveryAddress;
    private String userPhoneNumber;
    private OrderStatus status;
    private LocalDateTime startDeliveryTime;
    private LocalDateTime endDeliveryTime;
}
