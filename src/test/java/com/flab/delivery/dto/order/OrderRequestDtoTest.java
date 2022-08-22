package com.flab.delivery.dto.order;

import com.flab.delivery.dto.menu.MenuDto;
import com.flab.delivery.dto.option.OptionDto;
import com.flab.delivery.dto.order.user.OrderMenuDto;
import com.flab.delivery.dto.order.user.OrderRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class OrderRequestDtoTest {

    @Test
    void getTotalPrice_메뉴만_가격_확인() {
        // given
        OrderRequestDto requestDto = OrderRequestDto.builder()
                .menuList(
                        Arrays.asList(
                                createOrderMenu(1000, 1),
                                createOrderMenu(2000, 2),
                                createOrderMenu(3000, 3))
                ).build();
        // when
        int totalPrice = requestDto.getTotalPrice();

        // then
        Assertions.assertEquals(totalPrice, 14000);
    }

    @Test
    void getTotalPrice_메뉴와옵션_가격_확인() {
        // given
        OrderRequestDto requestDto = OrderRequestDto.builder()
                .menuList(
                        Arrays.asList(
                                createOrderMenu(1000, 1, Arrays.asList(
                                        createOption(100),
                                        createOption(200),
                                        createOption(300))),
                                createOrderMenu(2000, 2, Arrays.asList(
                                        createOption(100))),
                                createOrderMenu(3000, 3, Arrays.asList(
                                        createOption(100),
                                        createOption(200)))
                ))
                .build();
        // when
        int totalPrice = requestDto.getTotalPrice();

        // then
        Assertions.assertEquals(totalPrice, 1600 + 4200 + 9900);
    }

    private OptionDto createOption(int price) {
        return OptionDto.builder().price(price).build();
    }
    private OrderMenuDto createOrderMenu(int price, int quantity) {
        return createOrderMenu(price, quantity, new ArrayList<>());
    }
    private OrderMenuDto createOrderMenu(int price, int quantity, List<OptionDto> optionList) {
        return OrderMenuDto.builder()
                .menuDto(MenuDto.builder().price(price).build())
                .quantity(quantity).optionList(optionList)
                .build();
    }

}