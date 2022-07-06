package com.flab.delivery.mapper;

import com.flab.delivery.dto.SignUpDto;

public interface CommonMapper {

    void save(SignUpDto signUpDto);

    boolean existsById(String id);

    long getCountById();
}
