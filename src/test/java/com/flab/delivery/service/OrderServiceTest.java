package com.flab.delivery.service;

import com.flab.delivery.dto.order.OrderDto;
import com.flab.delivery.dto.order.OrderRequestDto;
import com.flab.delivery.exception.AddressException;
import com.flab.delivery.fixture.TestDto;
import com.flab.delivery.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private MockPayService payService;

    @Mock
    private AddressService addressService;

    @Mock
    private OrderMapper orderMapper;

    @Test
    void createOrder_대표주소_없어서_실패() {
        // given
        String userId = "wrongUser";
        OrderRequestDto requestDto = TestDto.getOrderRequestDto();

        doThrow(AddressException.class)
                .when(addressService)
                .getDeliveryAddress(eq(userId));

        // when
        assertThatThrownBy(() -> orderService.createOrder(userId, requestDto))
                .isInstanceOf(AddressException.class);

        // then
        verify(orderMapper, never()).save(eq(userId), any());
        verify(orderMapper, never()).changeStatus(anyLong(), any());
        verify(payService, never()).pay(anyLong(), any());
    }
    @Test
    void createOrder_잘못된_유저_실패() {
        // given
        String userId = "wrongUser";
        OrderRequestDto requestDto = TestDto.getOrderRequestDto();

        given(addressService.getDeliveryAddress(eq(userId))).willReturn("운암동 15번길 13");

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

    @Test
    void createOrder_성공() {
        // given
        String userId = "user1";
        OrderRequestDto requestDto = TestDto.getOrderRequestDto();

        ArgumentCaptor<OrderDto> valueCapture = ArgumentCaptor.forClass(OrderDto.class);

        given(addressService.getDeliveryAddress(eq(userId))).willReturn("운암동 15번길 13");
        // when
        orderService.createOrder(userId, requestDto);

        // then
        verify(orderMapper).save(eq(userId), valueCapture.capture());
        Long id = valueCapture.getValue().getId();
        verify(orderMapper).changeStatus(eq(id), any());
        verify(payService).pay(eq(id), any());
    }
}