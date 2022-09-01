package com.flab.delivery.mapper;

import com.flab.delivery.annotation.IntegrationTest;
import com.flab.delivery.dto.store.StoreDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
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


    @Test
    void existsByUserIdAndStoreId_조건에_맞는_매장이_존재해서_True_반환() {
        // given
        String userId = "user2";
        Long storeId = 2L;

        // when
        boolean result = storeMapper.existsByUserIdAndStoreId(userId, storeId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void existsByUserIdAndStoreId_조건에_맞는_매장이_존재하지_않아서_False_반환() {
        // given
        String userId = "user1";
        Long storeId = 2L;

        // when
        boolean result = storeMapper.existsByUserIdAndStoreId(userId, storeId);

        // then
        assertThat(result).isFalse();
    }
}