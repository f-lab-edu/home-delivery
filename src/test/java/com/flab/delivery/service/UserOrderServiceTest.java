package com.flab.delivery.service;

import com.flab.delivery.dto.order.OrderDto;
import com.flab.delivery.dto.order.user.OrderRequestDto;
import com.flab.delivery.dto.order.user.OrderSimpleResponseDto;
import com.flab.delivery.exception.AddressException;
import com.flab.delivery.fixture.TestDto;
import com.flab.delivery.mapper.OrderMapper;
import com.flab.delivery.utils.FCMAlarmConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.flab.delivery.fixture.TestDto.getOrderSimpleResponseDto;
import static com.flab.delivery.utils.FCMAlarmConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserOrderServiceTest {

    public static final String USER_ID = "user1";
    @InjectMocks
    private UserOrderService userOrderService;

    @Mock
    private MockPayService payService;

    @Mock
    private AddressService addressService;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private FCMService fcmService;

    @Test
    void createOrder_대표주소_없어서_실패() {
        // given
        String userId = "wrongUser";
        OrderRequestDto requestDto = TestDto.getOrderRequestDto();

        doThrow(AddressException.class)
                .when(addressService)
                .getDeliveryAddress(eq(userId));

        // when
        assertThatThrownBy(() -> userOrderService.createOrder(userId, requestDto))
                .isInstanceOf(AddressException.class);

        // then
        verify(orderMapper, never()).save(eq(userId), any());
        verify(orderMapper, never()).changeStatus(anyLong(), any());
        verify(payService, never()).pay(anyLong(), any());
        verify(fcmService, never()).sendMessage(userId, REQUEST_COMPLETE_TITLE, REQUEST_COMPLETE);
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
        assertThatThrownBy(() -> userOrderService.createOrder(userId, requestDto))
                .isInstanceOf(RuntimeException.class);

        // then
        verify(orderMapper).save(eq(userId), any());
        verify(orderMapper, never()).changeStatus(anyLong(), any());
        verify(payService, never()).pay(anyLong(), any());
        verify(fcmService, never()).sendMessage(userId, REQUEST_COMPLETE_TITLE, REQUEST_COMPLETE);
    }

    @Test
    void createOrder_성공() {
        // given
        String userId = USER_ID;
        OrderRequestDto requestDto = TestDto.getOrderRequestDto();

        ArgumentCaptor<OrderDto> valueCapture = ArgumentCaptor.forClass(OrderDto.class);

        given(addressService.getDeliveryAddress(eq(userId))).willReturn("운암동 15번길 13");
        // when
        userOrderService.createOrder(userId, requestDto);

        // then
        verify(orderMapper).save(eq(userId), valueCapture.capture());
        Long id = valueCapture.getValue().getId();
        verify(orderMapper).changeStatus(eq(id), any());
        verify(payService).pay(eq(id), any());
        verify(fcmService, times(1)).sendMessage(userId, REQUEST_COMPLETE_TITLE, REQUEST_COMPLETE);
    }


    @Test
    void getUserOrderList_주문목록_없음() {
        // given
        given(orderMapper.findPageIds(any(), eq(0))).willReturn(new ArrayList<>());

        // when
        List<OrderSimpleResponseDto> userOrderList = userOrderService.getUserOrderList(USER_ID, 0);

        // then
        assertThat(userOrderList.size()).isEqualTo(0);

        verify(orderMapper).findPageIds(eq(USER_ID), eq(0));
        verify(orderMapper, never()).findAllByPageIds(anyList());
    }

    @Test
    void getUserOrderList_주문목록_존재() {
        // given

        List<OrderSimpleResponseDto> responseDtoList = Arrays.asList(
                getOrderSimpleResponseDto("치킨", 10000),
                getOrderSimpleResponseDto("피자", 13000));

        List<Long> idList = Arrays.asList(1L, 2L, 3L);
        given(orderMapper.findPageIds(any(), eq(0))).willReturn(idList);
        given(orderMapper.findAllByPageIds(idList)).willReturn(responseDtoList);

        // when
        List<OrderSimpleResponseDto> userOrderList = userOrderService.getUserOrderList(USER_ID, 0);

        // then
        assertThat(userOrderList).usingFieldByFieldElementComparator().containsAll(responseDtoList);

        verify(orderMapper).findAllByPageIds(eq(idList));
        verify(orderMapper).findPageIds(any(), eq(0));
    }
}