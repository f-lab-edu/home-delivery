package com.flab.delivery.mapper;

import com.flab.delivery.dto.LoginDto;
import com.flab.delivery.dto.MemberDto;
import com.flab.delivery.dto.SignUpDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper extends CommonMapper {

    @Override
    void save(SignUpDto signUpDto);

    @Override
    boolean existsById(String id);

    @Override
    long getCountById();

    @Override
    Optional<MemberDto> findMemberById(String id);
}
