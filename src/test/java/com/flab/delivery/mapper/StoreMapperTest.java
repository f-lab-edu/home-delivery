package com.flab.delivery.mapper;

import com.flab.delivery.config.DatabaseConfig;
import com.flab.delivery.dto.store.StoreDto;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@Import({DatabaseConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StoreMapperTest {

    @Autowired
    private StoreMapper storeMapper;

    @Test
    void findStoreListBy_오픈중인_매장_없어서_빈리스트() {
        // given
        Long categoryId = 1L;
        Long addressId = 1L;

        // when
        List<StoreDto> storeListBy = storeMapper.findStoreListBy(categoryId, addressId);

        // then
        assertThat(storeListBy).isEmpty();
    }


    @Test
    void findStoreListBy_오픈중인_매장이_있어서_반환() {
        // given
        Long categoryId = 1L;
        Long addressId = 2L;

        // when
        List<StoreDto> storeListBy = storeMapper.findStoreListBy(categoryId, addressId);

        // then
        assertThat(storeListBy.size()).isGreaterThanOrEqualTo(2);
    }
}