package com.flab.delivery.service;

import com.flab.delivery.dto.menu.MenuDto;
import com.flab.delivery.dto.order.OrderRequestDto;
import com.flab.delivery.enums.PayType;
import com.flab.delivery.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private MockPayService payService;

    @Mock
    private OrderMapper orderMapper;

    @Test
    void createOrder_잘못된_유저_실패() {
        // given
        String userId = "wrongUser";
        OrderRequestDto requestDto = OrderRequestDto
                .builder()
                .payType(PayType.CARD)
                .deliveryPrice(3000)
                .storeId(1L)
                .menuList(
                        Arrays.asList(
                                createMenu(1L, "치킨", "오븐 치킨", 13000),
                                createMenu(2L, "피자", "고구마 피자", 15000)
                        )
                ).build();

        doThrow(RuntimeException.class)
                .when(orderMapper)
                .save(anyString(), any());

        // when
        assertThatThrownBy(() -> orderService.createOrder(userId, requestDto))
                .isInstanceOf(RuntimeException.class);

        // then
        verify(orderMapper).save(eq(userId), any());
        verify(orderMapper, never()).changeStatus(anyLong(), any());
        verify(payService, never()).pay(anyLong(), any());
    }

    private MenuDto createMenu(long id, String name, String info, int price) {
        return MenuDto.builder()
                .id(id)
                .name(name)
                .info(info)
                .price(price)
                .build();
    }

}