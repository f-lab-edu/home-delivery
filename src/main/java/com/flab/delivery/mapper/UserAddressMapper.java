package com.flab.delivery.mapper;

import com.flab.delivery.dto.address.AddressDto;
import com.flab.delivery.dto.address.AddressRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserAddressMapper {
    Long addAddress(@Param("addressDto") AddressRequestDto addressRequestDto,
                    @Param("addressId") Long findAddressId,
                    @Param("userId") String userId);

    List<AddressDto> findAllByUserId(String userId);

    boolean existsById(Long id);

    void deleteById(Long id);
}
