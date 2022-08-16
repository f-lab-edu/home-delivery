package com.flab.delivery.mapper;

import com.flab.delivery.dto.address.AddressDto;
import com.flab.delivery.dto.address.AddressRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserAddressMapper {
    int addAddress(@Param("addressDto") AddressRequestDto addressRequestDto,
                   @Param("addressId") Long findAddressId,
                   @Param("userId") String userId);

    List<AddressDto> findAllByUserId(String userId);

    boolean existsById(Long id);

    void deleteById(@Param("id") Long id,
                    @Param("userId") String userId);

    int resetSelection(String userId);

    AddressDto findById(Long id);

    int changeAddress(@Param("id") Long id,
                      @Param("userId") String userId);

    @Select("SELECT a.town_name , u.detail_address FROM ADDRESS a JOIN USER_ADDRESS u ON a.id = u.address_id AND u.selected = true")
    Optional<AddressDto> findDeliveryAddressByUserId(String userId);
}
