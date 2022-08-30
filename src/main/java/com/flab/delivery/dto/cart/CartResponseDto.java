package com.flab.delivery.dto.cart;

import lombok.Getter;

import java.util.List;

@Getter
public class CartResponseDto {

    private Long storeId;
    private List<ItemDto> cartList;

    public CartResponseDto(Long storeId, List<ItemDto> cartList) {
        this.storeId = storeId;
        this.cartList = cartList;
    }
}
