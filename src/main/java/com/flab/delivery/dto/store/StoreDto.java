package com.flab.delivery.dto.store;

import com.flab.delivery.enums.StoreStatus;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreDto {

    private Long id;

    private Long addressId;

    private Long categoryId;

    private String ownerId;

    private String detailAddress;

    private String name;

    private String phoneNumber;

    private String info;

    private StoreStatus status;

    private String openTime;

    private String endTime;

    private Long minPrice;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
