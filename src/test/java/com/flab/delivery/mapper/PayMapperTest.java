package com.flab.delivery.mapper;

import com.flab.delivery.AbstractDockerContainer;
import com.flab.delivery.config.DatabaseConfig;
import com.flab.delivery.dto.order.OrderDto;
import com.flab.delivery.dto.pay.PayDto;
import com.flab.delivery.enums.PayType;
import com.flab.delivery.fixture.TestDto;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

@MybatisTest
@Import({DatabaseConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PayMapperTest  extends AbstractDockerContainer {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private PayMapper payMapper;

    @Test
    void save_확인() {
        // given
        OrderDto orderDto = TestDto.getOrderDto();
        orderMapper.save("user1", orderDto);


        PayDto payDto = PayDto.completePay(orderDto.getId(), PayType.CARD);

        // when
        // then
        payMapper.save(payDto);
    }
}