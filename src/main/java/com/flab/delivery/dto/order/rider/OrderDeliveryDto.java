package com.flab.delivery.dto.order.rider;


import com.flab.delivery.enums.OrderStatus;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "orderId")
public class OrderDeliveryDto {

    private Long orderId;
    private Long addressId;
    private String storeName;
    private String storeAddress;
    private String deliveryAddress;
    private String userPhoneNumber;
    private OrderStatus status;
}
