package com.flab.delivery.mapper;

import com.flab.delivery.annotation.IntegrationTest;
import com.flab.delivery.dto.order.OrderDto;
import com.flab.delivery.dto.order.owner.OwnerOrderResponseDto;
import com.flab.delivery.dto.order.rider.OrderDeliveryDto;
import com.flab.delivery.dto.order.user.OrderSimpleResponseDto;
import com.flab.delivery.dto.pay.PayDto;
import com.flab.delivery.enums.OrderStatus;
import com.flab.delivery.enums.PayStatus;
import com.flab.delivery.enums.PayType;
import com.flab.delivery.fixture.TestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
class OrderMapperTest {

    public static final String RIDER_ID = "rider1";
    public static final String USER_ID = "user1";
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private PayMapper payMapper;

    @Test
    void save_확인() {
        // given
        OrderDto orderDto = saveOrderDto(USER_ID);

        // then
        assertThat(orderDto.getId()).isNotNull();
    }

    @Test
    void save_잘못된_유저_실패() {
        // given
        OrderDto orderDto = TestDto.getOrderDto();

        // when
        // then
        assertThatThrownBy(() -> orderMapper.save("wrongUser", orderDto))
                .isInstanceOf(DataAccessException.class)
                .getCause().isInstanceOf(SQLIntegrityConstraintViolationException.class);

    }

    @Test
    void changeStatus_확인() {
        // given
        OrderDto orderDto = saveOrderDto(USER_ID);

        // when
        Long count = orderMapper.changeStatus(orderDto.getId(), OrderStatus.ORDER_APPROVED);

        // then
        assertThat(count).isEqualTo(1);
    }

    @Test
    void findAllByPageIds_확인() {
        // given
        for (int i = 0; i < 15; i++) {
            saveOrderDto(USER_ID);
        }

        // when
        List<OrderSimpleResponseDto> allByPageIds = orderMapper.findAllByPageIds(USER_ID, null);

        // then
        assertThat(allByPageIds.size()).isEqualTo(10);
        assertThat(allByPageIds).usingFieldByFieldElementComparator().isNotNull();

    }


    @Test
    void findByIdAndUserId_확인() {
        // given
        OrderDto dto = TestDto.getOrderDto();
        String userId = USER_ID;
        orderMapper.save(userId, dto);
        payMapper.save(PayDto.builder().type(PayType.CARD).orderId(dto.getId()).status(PayStatus.COMPLETE).build());

        // when
        OrderDto findOrder = orderMapper.findByOrderId(dto.getId());

        // then
        assertThat(findOrder.getOrderPrice()).isEqualTo(dto.getOrderPrice());
        assertThat(findOrder.getDeliveryAddress()).isEqualTo(dto.getDeliveryAddress());
        assertThat(findOrder.getCreatedAt()).isBefore(LocalDateTime.now());
        assertThat(findOrder.getHistory()).usingRecursiveComparison().isEqualTo(dto.getHistory());
        assertThat(findOrder.getPayType()).isNotNull();
    }


    @Test
    void findAllOwnerOrder_확인() {
        // given
        orderMapper.save(USER_ID, TestDto.getOrderDto());
        orderMapper.save("user2", TestDto.getOrderDto());


        // when
        List<OwnerOrderResponseDto> responseDtoList = orderMapper.findAllOwnerOrderLimit100(1L);

        // then
        for (OwnerOrderResponseDto dto : responseDtoList) {
            assertThat(dto).usingRecursiveComparison().isNotNull();
        }
    }

    @Test
    void findDeliveryInfo_검색_결과_없음() {
        // given
        OrderDto orderDto = saveOrderDto(USER_ID);

        // when
        Optional<OrderDeliveryDto> deliveryInfo = orderMapper.findDeliveryInfo("user3", orderDto.getId(), orderDto.getStoreId());

        // then
        assertThat(deliveryInfo).isEmpty();
    }

    @Test
    void findDeliveryInfo_확인() {
        // given
        OrderDto orderDto = saveOrderDto(USER_ID);

        // when
        Optional<OrderDeliveryDto> deliveryInfo = orderMapper.findDeliveryInfo("user2", orderDto.getId(), orderDto.getStoreId());

        // then
        assertThat(deliveryInfo.get()).usingRecursiveComparison().isNotNull();
    }

    @Test
    void updateOrderForDelivery_확인() {
        // given
        OrderDto orderDto = saveOrderDto(USER_ID);

        // when
        Long result = orderMapper.updateOrderForDelivery(orderDto.getId(), RIDER_ID);

        // then
        assertThat(result).isEqualTo(1);
    }

    @Test
    void updateOrderForFinish_주문_상태가_배달중이_아니라서_변경_X() {
        // given
        OrderDto orderDto = saveOrderDto(USER_ID);

        // when
        Long result = orderMapper.updateOrderForFinish(orderDto.getId(), RIDER_ID);

        // then
        assertThat(result).isEqualTo(0);
    }

    @Test
    void updateOrderForFinish_확인() {
        // given
        OrderDto orderDto = saveOrderDto(USER_ID);

        // when
        orderMapper.updateOrderForDelivery(orderDto.getId(), RIDER_ID);
        Long result = orderMapper.updateOrderForFinish(orderDto.getId(), RIDER_ID);

        // then
        assertThat(result).isEqualTo(1);
    }

    @Test
    void findInDeliveryList_확인() {
        // given
        List<Long> orderIds = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            OrderDto dto = saveOrderDto(USER_ID);
            orderIds.add(dto.getId());
            orderMapper.updateOrderForDelivery(dto.getId(), RIDER_ID);
        }

        // when
        List<OrderDeliveryDto> inDeliveryList = orderMapper.findInDeliveryList(RIDER_ID);

        // then
        assertThat(inDeliveryList).usingFieldByFieldElementComparator().isNotNull();
        assertThat(inDeliveryList).extracting(OrderDeliveryDto::getOrderId).containsAnyElementsOf(orderIds);
    }

    @Test
    void findFinishDeliveryList_확인() {
        // given

        for (int i = 0; i < 15; i++) {
            OrderDto dto = saveOrderDto(USER_ID);
            orderMapper.updateOrderForDelivery(dto.getId(), RIDER_ID);
            orderMapper.updateOrderForFinish(dto.getId(), RIDER_ID);
        }

        // when
        List<OrderDeliveryDto> finishDeliveryList = orderMapper.findFinishDeliveryList(RIDER_ID, null);

        // then
        assertThat(finishDeliveryList).usingFieldByFieldElementComparator().isNotNull();
        assertThat(finishDeliveryList.size()).isEqualTo(10);

    }

    @ParameterizedTest
    @ValueSource(longs = {10, 30, 50})
    void findFinishDeliveryList_n번째_이후_배달_완료_목록_확인(long page) {
        // given
        long startId = 0;
        for (int i = 0; i < 100; i++) {
            OrderDto dto = saveOrderDto(USER_ID);
            orderMapper.updateOrderForDelivery(dto.getId(), RIDER_ID);
            orderMapper.updateOrderForFinish(dto.getId(), RIDER_ID);

            if (i == 99) {
                startId = dto.getId();
            }
        }

        // when
        List<OrderDeliveryDto> finishDeliveryList = orderMapper.findFinishDeliveryList(RIDER_ID, startId - page);

        // then
        assertThat(finishDeliveryList).usingFieldByFieldElementComparator().isNotNull();
        assertThat(finishDeliveryList.size()).isEqualTo(10);

    }

    private OrderDto saveOrderDto(String userId) {
        OrderDto dto = TestDto.getOrderDto();
        orderMapper.save(userId, dto);
        return dto;
    }


}