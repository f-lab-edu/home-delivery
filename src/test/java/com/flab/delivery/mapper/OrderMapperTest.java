package com.flab.delivery.mapper;

import com.flab.delivery.config.DatabaseConfig;
import com.flab.delivery.dto.order.owner.OwnerOrderResponseDto;
import com.flab.delivery.dto.order.OrderDto;
import com.flab.delivery.dto.order.user.OrderSimpleResponseDto;
import com.flab.delivery.dto.pay.PayDto;
import com.flab.delivery.enums.OrderStatus;
import com.flab.delivery.enums.PayStatus;
import com.flab.delivery.enums.PayType;
import com.flab.delivery.fixture.TestDto;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@MybatisTest
@Import({DatabaseConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderMapperTest {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private PayMapper payMapper;

    @Test
    void save_확인() {
        // given
        OrderDto orderDto = TestDto.getOrderDto();

        // when
        orderMapper.save("user1", orderDto);

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
        OrderDto orderDto = TestDto.getOrderDto();
        orderMapper.save("user1", orderDto);

        // when
        Long count = orderMapper.changeStatus(orderDto.getId(), OrderStatus.ORDER_APPROVED);

        // then
        assertThat(count).isEqualTo(1);
    }

    @Test
    void findAllByPageIds_확인() {
        // given
        List<Long> orderIds = new ArrayList<>();

        OrderDto orderDto = TestDto.getOrderDto();
        for (int i = 0; i < 15; i++) {
            OrderDto dto = TestDto.getOrderDto();
            orderMapper.save("user1", dto);
            orderIds.add(dto.getId());
        }

        assertThat(orderIds.size()).isEqualTo(15);

        // when
        List<OrderSimpleResponseDto> allByPageIds = orderMapper.findAllByPageIds(orderIds);

        // then
        assertThat(allByPageIds.size()).isEqualTo(10);

        for (int i = 0; i < 10; i++) {
            OrderSimpleResponseDto simpleResponseDto = allByPageIds.get(i);
            assertThat(simpleResponseDto.getOrderPrice()).isEqualTo(orderDto.getOrderPrice());
            assertThat(simpleResponseDto.getStoreId()).isEqualTo(orderDto.getStoreId());
            assertThat(simpleResponseDto.getCreatedAt()).isBefore(LocalDateTime.now());
            assertThat(simpleResponseDto.getMenuName()).isEqualTo(orderDto.getHistory().getMenuList().get(0).getMenuName());
            assertThat(simpleResponseDto.getMenuCount()).isEqualTo(orderDto.getHistory().getMenuCount());
            assertThat(simpleResponseDto.getStatus()).isEqualTo(orderDto.getStatus());

        }
    }


    @Test
    void findByIdAndUserId_확인() {
        // given
        OrderDto dto = TestDto.getOrderDto();
        String userId = "user1";
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
        orderMapper.save("user1", TestDto.getOrderDto());
        orderMapper.save("user2", TestDto.getOrderDto());



        // when
        List<OwnerOrderResponseDto> responseDtoList = orderMapper.findAllOwnerOrderLimit100(1L);

        // then
        for (OwnerOrderResponseDto dto : responseDtoList) {
            assertThat(dto).usingRecursiveComparison().isNotNull();
        }
    }

}