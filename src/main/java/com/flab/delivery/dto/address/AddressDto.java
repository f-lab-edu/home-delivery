package com.flab.delivery.dto.address;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressDto {

    private Long id;
    private String townName;
    private String detailAddress;
    private String alias;
    private boolean selected;

}
