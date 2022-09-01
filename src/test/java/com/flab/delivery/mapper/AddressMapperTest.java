package com.flab.delivery.mapper;

import com.flab.delivery.AbstractDockerContainer;
import com.flab.delivery.config.DatabaseConfig;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@Import({DatabaseConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AddressMapperTest extends AbstractDockerContainer {

    @Autowired
    private AddressMapper addressMapper;

    @Test
    void findIdByTownName_존재하는_주소_성공() {
        // given
        String townName = "운암동";

        // when
        Long byTownName = addressMapper.findIdByTownName(townName);

        //then
        assertThat(byTownName).isEqualTo(1L);
    }

    @Test
    void findIdByTownName_존재하지_않는_주소_실패() {
        // given
        String townName = "운암동2";

        // when
        Long byTownName = addressMapper.findIdByTownName(townName);

        //then
        assertThat(byTownName).isNull();
    }
}