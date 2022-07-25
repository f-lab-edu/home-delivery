package com.flab.delivery.mapper;

import com.flab.delivery.dto.store.StoreDto;
import com.flab.delivery.dto.store.StoreRequestDto;
import com.flab.delivery.enums.StoreStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface StoreMapper {

    void save(StoreRequestDto storeRequestDto, @Param("userId") String userId);

    Optional<Long> existsByNameAndDetailAddress(@Param("name") String name, @Param("detailAddress") String detailAddress);

    List<StoreDto> findAllByUserId(String userId);

    Optional<StoreDto> findById(@Param("id") Long storeId);

    void updateById(@Param("id") Long storeId, StoreRequestDto storeRequestDto);

    void deleteById(@Param("id") Long storeId);

    void updateStatusById(@Param("id") Long storeId, @Param("status") StoreStatus status);
}
