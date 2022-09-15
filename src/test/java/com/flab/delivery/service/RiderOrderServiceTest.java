package com.flab.delivery.service;

import com.flab.delivery.dao.RiderDao;
import com.flab.delivery.dto.order.rider.OrderDeliveryDto;
import com.flab.delivery.exception.OrderException;
import com.flab.delivery.fixture.TestDto;
import com.flab.delivery.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.flab.delivery.exception.message.ErrorMessageConstants.NOT_EXIST_ORDER_REQUEST_MESSAGE;
import static com.flab.delivery.exception.message.ErrorMessageConstants.NOT_STAND_BY_RIDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RiderOrderServiceTest {


    public static final String RIDER_ID = "rider1";
    public static final long ADDRESS_ID = 1L;
    public static final long ORDER_ID = 1L;
    @InjectMocks
    RiderOrderService riderOrderService;

    @Mock
    RiderDao riderDao;

    @Mock
    OrderMapper orderMapper;


    @Test
    void getDeliveryRequests_출근중이_아닌_라이더_실패() {
        // given
        givenStandByRiderFrom(false);

        // when
        assertThatThrownBy(() -> riderOrderService.getDeliveryRequests(RIDER_ID, ADDRESS_ID))
                .isInstanceOf(OrderException.class)
                .hasMessage(NOT_STAND_BY_RIDER);

        // then
        verify(riderDao, never()).getDeliveryRequestList(eq(ADDRESS_ID));
    }

    @Test
    void getDeliveryRequests_성공() {
        // given
        givenStandByRiderFrom(true);
        given(riderDao.getDeliveryRequestList(ADDRESS_ID)).willReturn(Arrays.asList(TestDto.getOrderDeliveryDto(ORDER_ID)));

        // when
        List<OrderDeliveryDto> deliveryRequests = riderOrderService.getDeliveryRequests(RIDER_ID, ADDRESS_ID);

        // then
        assertThat(deliveryRequests.size()).isEqualTo(1);
        verify(riderDao).getDeliveryRequestList(eq(ADDRESS_ID));
    }

    @Test
    void acceptDeliveryBy_출근중이_아닌_라이더_실패() {
        // given
        givenStandByRiderFrom(false);

        // when
        assertThatThrownBy(() -> riderOrderService.acceptDeliveryBy(ORDER_ID, RIDER_ID, ADDRESS_ID))
                .isInstanceOf(OrderException.class)
                .hasMessage(NOT_STAND_BY_RIDER);

        // then
        verify(riderDao, never()).acceptDelivery(eq(ADDRESS_ID), any());
        verify(orderMapper, never()).updateOrderForDelivery(eq(ORDER_ID), eq(RIDER_ID));

    }


    @Test
    void acceptDeliveryBy_존재하지_않는_배차목록_수락_실패() {
        // given
        givenStandByRiderFrom(true);

        // when
        assertThatThrownBy(() -> riderOrderService.acceptDeliveryBy(ORDER_ID, RIDER_ID, ADDRESS_ID))
                .isInstanceOf(OrderException.class)
                .hasMessage(NOT_EXIST_ORDER_REQUEST_MESSAGE);

        // then
        verify(orderMapper, never()).updateOrderForDelivery(eq(ORDER_ID), eq(RIDER_ID));
        verify(riderDao, never()).acceptDelivery(eq(ORDER_ID), any());

    }

    @Test
    void acceptDeliveryBy_이미_수락된_배차_수락하여_실패() {
        // given
        givenStandByRiderFrom(true);
        given(riderDao.findDeliveryRequest(any(), any())).willReturn(TestDto.getOrderDeliveryDto(ORDER_ID));

        // when
        assertThatThrownBy(() -> riderOrderService.acceptDeliveryBy(ORDER_ID, RIDER_ID, ADDRESS_ID))
                .isInstanceOf(OrderException.class)
                .hasMessage(NOT_EXIST_ORDER_REQUEST_MESSAGE);

        // then
        verify(orderMapper, never()).updateOrderForDelivery(eq(ORDER_ID), eq(RIDER_ID));

    }


    @Test
    void acceptDeliveryBy_성공() {
        // given
        givenStandByRiderFrom(true);
        given(riderDao.findDeliveryRequest(any(), any())).willReturn(TestDto.getOrderDeliveryDto(ORDER_ID));
        given(riderDao.acceptDelivery(any(), any())).willReturn(true);

        // when
        riderOrderService.acceptDeliveryBy(ORDER_ID, RIDER_ID, ADDRESS_ID);

        // then
        verify(orderMapper).updateOrderForDelivery(eq(ORDER_ID), eq(RIDER_ID));

    }

    @Test
    void finishDeliveryBy_출근중이_아닌_라이더_실패() {
        // given
        givenStandByRiderFrom(false);

        // when
        assertThatThrownBy(() -> riderOrderService.finishDeliveryBy(ORDER_ID, RIDER_ID, ADDRESS_ID))
                .isInstanceOf(OrderException.class)
                .hasMessage(NOT_STAND_BY_RIDER);

        // then
        verify(orderMapper, never()).updateOrderForFinish(eq(ORDER_ID), eq(RIDER_ID));
    }

    @Test
    void finishDeliveryBy_성공() {
        // given
        givenStandByRiderFrom(true);

        // when
        riderOrderService.finishDeliveryBy(ORDER_ID, RIDER_ID, ADDRESS_ID);

        // then
        verify(orderMapper).updateOrderForFinish(eq(ORDER_ID), eq(RIDER_ID));

    }

    @Test
    void getInDeliveryList_출근중이_아닌_라이더_실패() {
        // given
        givenStandByRiderFrom(false);

        // when
        assertThatThrownBy(() -> riderOrderService.getInDeliveryList(RIDER_ID, ADDRESS_ID))
                .isInstanceOf(OrderException.class)
                .hasMessage(NOT_STAND_BY_RIDER);

        // then
        verify(orderMapper, never()).findInDeliveryList(eq(RIDER_ID));
    }

    @Test
    void getInDeliveryList_성공() {

        // given
        givenStandByRiderFrom(true);
        given(orderMapper.findInDeliveryList(eq(RIDER_ID))).willReturn(Arrays.asList(
                TestDto.getOrderDeliveryDto(ORDER_ID)
        ));

        // when
        List<OrderDeliveryDto> inDeliveryList = riderOrderService.getInDeliveryList(RIDER_ID, ADDRESS_ID);

        // then
        assertThat(inDeliveryList.size()).isEqualTo(1);
        verify(orderMapper).findInDeliveryList(eq(RIDER_ID));

    }

    @Test
    void getFinishDeliveryList_출근중이_아닌_라이더_실패() {
        // given
        givenStandByRiderFrom(false);

        // when
        assertThatThrownBy(() -> riderOrderService.getFinishDeliveryList(RIDER_ID, ADDRESS_ID, null))
                .isInstanceOf(OrderException.class)
                .hasMessage(NOT_STAND_BY_RIDER);

        // then
        verify(orderMapper, never()).findFinishDeliveryList(eq(RIDER_ID), any());
    }

    @Test
    void getFinishDeliveryList_배달완료된_주문이_존재하지_않아서_빈리스트_반환() {
        // given
        givenStandByRiderFrom(true);
        given(orderMapper.findFinishDeliveryList(eq(RIDER_ID), any())).willReturn(new ArrayList<>());

        // when
        List<OrderDeliveryDto> finishDeliveryList = riderOrderService.getFinishDeliveryList(RIDER_ID, ADDRESS_ID, null);

        // then
        assertThat(finishDeliveryList).isEmpty();
    }

    @Test
    void getFinishDeliveryList_성공() {
        // given
        givenStandByRiderFrom(true);
        given(orderMapper.findFinishDeliveryList(eq(RIDER_ID), any())).willReturn(Arrays.asList(TestDto.getOrderDeliveryDto(ORDER_ID)));

        // when
        List<OrderDeliveryDto> finishDeliveryList = riderOrderService.getFinishDeliveryList(RIDER_ID, ADDRESS_ID, null);

        // then
        assertThat(finishDeliveryList).isNotEmpty();
        verify(orderMapper).findFinishDeliveryList(eq(RIDER_ID), any());
    }

    private void givenStandByRiderFrom(boolean value) {
        given(riderDao.isStandByRider(RIDER_ID, ADDRESS_ID)).willReturn(value);
    }
}