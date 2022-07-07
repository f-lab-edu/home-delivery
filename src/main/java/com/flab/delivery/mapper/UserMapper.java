package com.flab.delivery.mapper;

import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {

    void save(SignUpDto signUpDto);

    boolean existsUserById(String id);

    long getCountById();

    Optional<UserDto> findUserById(String id);
}
