package com.flab.delivery.mapper;

import com.flab.delivery.config.DatabaseConfig;
import com.flab.delivery.dto.order.OrderDto;
import com.flab.delivery.enums.OrderStatus;
import com.flab.delivery.fixture.TestDto;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;

import java.sql.SQLIntegrityConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@MybatisTest
@Import({DatabaseConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderMapperTest {

    @Autowired
    private OrderMapper orderMapper;


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


}