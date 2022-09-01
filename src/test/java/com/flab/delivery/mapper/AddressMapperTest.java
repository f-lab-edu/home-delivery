package com.flab.delivery.mapper;

import com.flab.delivery.annotation.DatabaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@DatabaseTest
class AddressMapperTest {

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