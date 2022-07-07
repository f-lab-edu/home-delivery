package com.flab.delivery.mapper;

import com.flab.delivery.dto.MemberDto;
import com.flab.delivery.dto.SignUpDto;

import java.util.Optional;

public interface CommonMapper {

    void save(SignUpDto signUpDto);

    boolean existsById(String id);

    long getCountById();

    Optional<MemberDto> findMemberById(String id);
}
