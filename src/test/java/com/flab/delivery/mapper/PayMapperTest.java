package com.flab.delivery.mapper;

import com.flab.delivery.config.DatabaseConfig;
import com.flab.delivery.dto.pay.PayDto;
import com.flab.delivery.enums.PayType;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

@MybatisTest
@Import({DatabaseConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PayMapperTest {

    @Autowired
    private PayMapper payMapper;

    @Test
    void save_확인() {
        // given
        PayDto payDto = PayDto.completePay(1L, PayType.CARD);

        // when
        // then
        payMapper.save(payDto);
    }
}