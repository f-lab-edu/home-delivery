package com.flab.delivery.mapper;

import com.flab.delivery.dto.SignUpDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RiderMapper extends CommonMapper {

    @Override
    void save(SignUpDto signUpDto);

    @Override
    boolean existsById(String id);

    @Override
    long getCountById();
}
