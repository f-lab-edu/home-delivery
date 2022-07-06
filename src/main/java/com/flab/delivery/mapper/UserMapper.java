package com.flab.delivery.mapper;

import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    void save(SignUpDto signUpDto);

    boolean existUserById(String id);

    long countUser();
}
