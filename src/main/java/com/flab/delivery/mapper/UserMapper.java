package com.flab.delivery.mapper;

import com.flab.delivery.dto.SignUpDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends CommonMapper {

    @Override
    void save(SignUpDto signUpDto);

    @Override
    boolean existById(String id);

    @Override
    long countById();
}
