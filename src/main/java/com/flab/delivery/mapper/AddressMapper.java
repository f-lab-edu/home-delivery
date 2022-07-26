package com.flab.delivery.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressMapper {

    Long findIdByTownName(String townName);
}
