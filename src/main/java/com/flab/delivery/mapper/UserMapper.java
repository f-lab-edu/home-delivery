package com.flab.delivery.mapper;

import com.flab.delivery.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    void save(UserDto userDto);

    boolean existUserById(String id);
}
