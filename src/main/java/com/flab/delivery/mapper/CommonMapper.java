package com.flab.delivery.mapper;

import com.flab.delivery.dto.SignUpDto;

public interface CommonMapper {

    void save(SignUpDto signUpDto);

    boolean existById(String id);

    long countById();
}
