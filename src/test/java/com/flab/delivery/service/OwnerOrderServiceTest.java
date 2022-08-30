package com.flab.delivery.service;

import com.flab.delivery.dao.RiderDao;
import com.flab.delivery.dto.order.OrderDto;
import com.flab.delivery.dto.order.owner.OwnerOrderResponseDto;
import com.flab.delivery.dto.order.rider.OrderDeliveryDto;
import com.flab.delivery.enums.OrderStatus;
import com.flab.delivery.exception.AddressException;
import com.flab.delivery.exception.OrderException;
import com.flab.delivery.fixture.TestDto;
import com.flab.delivery.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.flab.delivery.exception.message.ErrorMessageConstants.BAD_REQUEST_MESSAGE;
import static com.flab.delivery.exception.message.ErrorMessageConstants.NOT_ENOUGH_DELIVERY_REQUEST_TIME_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerOrderServiceTest {

    private static final String OWNER_ID = "owner";
    private static final long STORE_ID = 1L;
    private static final long ADDRESS_ID = 1L;
    private static final long ORDER_ID = 1L;
    @InjectMocks
    OwnerOrderService ownerOrderService;

    @Mock
    OrderMapper orderMapper;

    @Mock
    StoreService storeService;

    @Mock
    RiderDao riderDao;


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
        given(orderMapper.findByOrderId(anyLong())).willReturn(null);

        // when
        assertThatThrownBy(() -> ownerOrderService.approveOrder(OWNER_ID, STORE_ID)).isInstanceOf(OrderException.class);

        // then
        verify(orderMapper, never()).changeStatus(anyLong(), any());
    }

    @Test
    void approveOrder_주문에_대한_매장이_존재하지_않아서_예외_반환() {
        // given
        OrderDto orderDto = TestDto.getOrderDto(1L);
        given(orderMapper.findByOrderId(anyLong())).willReturn(orderDto);
        given(storeService.existsStoreByUserIdAndStoreId(eq(OWNER_ID), eq(1L))).willReturn(false);

        // when
        assertThatThrownBy(() -> ownerOrderService.approveOrder(OWNER_ID, STORE_ID)).isInstanceOf(OrderException.class);

        // then
        verify(orderMapper, never()).changeStatus(anyLong(), any());
    }

    @Test
    void approveOrder_성공() {
        // given
        OrderDto orderDto = TestDto.getOrderDto(1L);
        given(orderMapper.findByOrderId(anyLong())).willReturn(orderDto);
        given(storeService.existsStoreByUserIdAndStoreId(eq(OWNER_ID), eq(1L))).willReturn(true);

        // when
        ownerOrderService.approveOrder(OWNER_ID, STORE_ID);

        // then
        verify(orderMapper).changeStatus(eq(orderDto.getId()), any());
    }

    @Test
    void cancelOrder_잘못된_입력값으로_예외_반환() {
        // given
        given(orderMapper.findByOrderId(anyLong())).willReturn(null);

        // when
        assertThatThrownBy(() -> ownerOrderService.cancelOrder(OWNER_ID, STORE_ID)).isInstanceOf(OrderException.class);

        // then
        verify(orderMapper, never()).changeStatus(anyLong(), any());
    }

    @Test
    void cancelOrder_주문에_대한_매장이_존재하지_않아서_예외_반환() {
        // given
        OrderDto orderDto = TestDto.getOrderDto(1L);
        given(orderMapper.findByOrderId(anyLong())).willReturn(orderDto);
        given(storeService.existsStoreByUserIdAndStoreId(eq(OWNER_ID), eq(1L))).willReturn(false);

        // when
        assertThatThrownBy(() -> ownerOrderService.cancelOrder(OWNER_ID, STORE_ID)).isInstanceOf(OrderException.class);

        // then
        verify(orderMapper, never()).changeStatus(anyLong(), any());
    }


    @Test
    void cancelOrder_성공() {
        // given
        OrderDto orderDto = TestDto.getOrderDto(1L);
        given(orderMapper.findByOrderId(anyLong())).willReturn(orderDto);
        given(storeService.existsStoreByUserIdAndStoreId(eq(OWNER_ID), eq(1L))).willReturn(true);

        // when
        ownerOrderService.cancelOrder(OWNER_ID, STORE_ID);

        // then
        verify(orderMapper).changeStatus(eq(orderDto.getId()), any());
    }

    @Test
    void callRider_입력된_매장의_매장_주인이_아니라서_예외_반환() {
        // given
        given(orderMapper.findDeliveryInfo(eq(OWNER_ID), anyLong(), eq(STORE_ID))).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> ownerOrderService.callRider(OWNER_ID, ORDER_ID, STORE_ID))
                .isInstanceOf(OrderException.class)
                .hasMessage(BAD_REQUEST_MESSAGE);

        // then
        verify(riderDao, never()).addDeliveryRequestBy(anyLong(), any());
    }

    @Test
    void callRider_배차_요청후_최소_요청시간이_지나지_않아_예외_반환() {

        // given
        given(orderMapper.findDeliveryInfo(eq(OWNER_ID), anyLong(), eq(STORE_ID))).willReturn(Optional.of(OrderDeliveryDto.builder().addressId(ADDRESS_ID).build()));
        doThrow(OrderException.class).when(riderDao).addDeliveryRequestBy(eq(ADDRESS_ID),any());

        // when
        // then
        assertThatThrownBy(() -> ownerOrderService.callRider(OWNER_ID, ORDER_ID, STORE_ID))
                .isInstanceOf(OrderException.class);

    }

    @Test
    void callRider_성공() {

        // given
        given(orderMapper.findDeliveryInfo(eq(OWNER_ID), anyLong(), eq(STORE_ID))).willReturn(Optional.of(OrderDeliveryDto.builder().addressId(ADDRESS_ID).build()));

        // when
        // then
        ownerOrderService.callRider(OWNER_ID, ORDER_ID, STORE_ID);
    }

    private OwnerOrderResponseDto getOwnerOrderResponseDto(OrderStatus status, LocalDateTime createdAt) {
        return OwnerOrderResponseDto.builder()
                .status(status)
                .createdAt(createdAt)
                .build();
    }

}