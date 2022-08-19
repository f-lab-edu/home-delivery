package com.flab.delivery.service;

import com.flab.delivery.dto.order.OrderDto;
import com.flab.delivery.dto.order.owner.OwnerOrderResponseDto;
import com.flab.delivery.enums.OrderStatus;
import com.flab.delivery.exception.OrderException;
import com.flab.delivery.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OwnerOrderServiceTest {

    public static final String OWNER_ID = "owner";
    public static final long STORE_ID = 1L;
    @InjectMocks
    OwnerOrderService ownerOrderService;

    @Mock
    OrderMapper orderMapper;

    @Mock
    StoreService storeService;


    @Test
    void getOwnerOrderList_잘못된_입력값으로_빈리스트_반환() {

        // given
        given(storeService.existsStoreByUserIdAndStoreId(anyString(), anyLong())).willReturn(false);

        // when
        List<OwnerOrderResponseDto> ownerOrderList = ownerOrderService.getOwnerOrderList(OWNER_ID, STORE_ID);

        // then
        assertThat(ownerOrderList).isEmpty();

        verify(orderMapper, never()).findAllOwnerOrderLimit100(eq(STORE_ID));

    }


    @Test
    void getOwnerOrderList_결제전_및_3일이_지난_주문목록_제외_후_반환() {
        // given
        given(storeService.existsStoreByUserIdAndStoreId(anyString(), anyLong())).willReturn(true);
        given(orderMapper.findAllOwnerOrderLimit100(eq(STORE_ID))).willReturn(
                Arrays.asList(
                        getOwnerOrderResponseDto(OrderStatus.ORDER_REQUEST, LocalDateTime.now().minusDays(4)),
                        getOwnerOrderResponseDto(OrderStatus.BEFORE_PAYMENT, LocalDateTime.now().minusDays(2)),
                        getOwnerOrderResponseDto(OrderStatus.ORDER_APPROVED, LocalDateTime.now().minusDays(3)),
                        getOwnerOrderResponseDto(OrderStatus.ORDER_APPROVED, LocalDateTime.now().minusDays(1)),
                        getOwnerOrderResponseDto(OrderStatus.ORDER_REQUEST, LocalDateTime.now().minusDays(2)))
        );

        // when
        List<OwnerOrderResponseDto> ownerOrderList = ownerOrderService.getOwnerOrderList(OWNER_ID, STORE_ID);

        // then
        assertThat(ownerOrderList.size()).isEqualTo(2);
    }

    @Test
    void approveOrder_잘못된_입력값으로_예외_반환() {
        // given
        given(orderMapper.findByIdAndUserId(anyLong(), anyString())).willReturn(null);

        // when
        assertThatThrownBy(() -> ownerOrderService.approveOrder(OWNER_ID, STORE_ID)).isInstanceOf(OrderException.class);

        // then
        verify(orderMapper, never()).changeStatus(anyLong(), any());
    }

    @Test
    void approveOrder_성공() {
        // given
        OrderDto orderDto = OrderDto.builder().id(1L).status(OrderStatus.ORDER_REQUEST).build();
        given(orderMapper.findByIdAndUserId(anyLong(), anyString())).willReturn(orderDto);

        // when
        ownerOrderService.approveOrder(OWNER_ID, STORE_ID);

        // then
        verify(orderMapper).changeStatus(eq(orderDto.getId()), any());
    }

    @Test
    void cancelOrder_잘못된_입력값으로_예외_반환() {
        // given
        given(orderMapper.findByIdAndUserId(anyLong(), anyString())).willReturn(null);

        // when
        assertThatThrownBy(() -> ownerOrderService.cancelOrder(OWNER_ID, STORE_ID)).isInstanceOf(OrderException.class);

        // then
        verify(orderMapper, never()).changeStatus(anyLong(), any());
    }

    @Test
    void cancelOrder_성공() {
        // given
        OrderDto orderDto = OrderDto.builder().id(1L).status(OrderStatus.ORDER_REQUEST).build();
        given(orderMapper.findByIdAndUserId(anyLong(), anyString())).willReturn(orderDto);

        // when
        ownerOrderService.cancelOrder(OWNER_ID, STORE_ID);

        // then
        verify(orderMapper).changeStatus(eq(orderDto.getId()), any());
    }

    private OwnerOrderResponseDto getOwnerOrderResponseDto(OrderStatus status, LocalDateTime createdAt) {
        return OwnerOrderResponseDto.builder()
                .status(status)
                .createdAt(createdAt)
                .build();
    }

}