package com.flab.delivery.mapper;

import com.flab.delivery.dto.store.StoreDto;
import com.flab.delivery.dto.store.StoreRequestDto;
import com.flab.delivery.enums.StoreStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface StoreMapper {

    void save(@Param("store") StoreRequestDto storeRequestDto, @Param("userId") String userId);

    Optional<Long> existsByNameAndDetailAddress(@Param("name") String name, @Param("detailAddress") String detailAddress);

    List<StoreDto> findAllByUserId(String userId);

    Optional<StoreDto> findById(Long id);

    void updateById(@Param("id") Long storeId, @Param("store") StoreRequestDto storeRequestDto);

    void deleteById(Long id);

    void updateStatusById(@Param("id") Long id, @Param("status") StoreStatus status);

    List<StoreDto> findStoreListBy(@Param("categoryId") Long categoryId, @Param("addressId") Long addressId);
}
