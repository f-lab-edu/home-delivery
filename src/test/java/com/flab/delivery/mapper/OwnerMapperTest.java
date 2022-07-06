package com.flab.delivery.mapper;

import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.TestDto;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@MybatisTest
class OwnerMapperTest {

    @Autowired
    OwnerMapper ownerMapper;

    @Test
    void save_확인() {

        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();

        // when
        ownerMapper.save(signUpDto);

        //then
        assertThat(ownerMapper.existById(signUpDto.getId())).isTrue();
    }

    @Test
    void save_이미_존재하는_PK로_인한_실패() {

        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();

        // when
        ownerMapper.save(signUpDto);
        assertThatThrownBy(() -> ownerMapper.save(signUpDto)).isInstanceOf(DataAccessException.class);

        //then
        assertThat(ownerMapper.existById(signUpDto.getId())).isTrue();
        assertThat(ownerMapper.countById()).isEqualTo(1);
    }

    @Test
    void existOwnerById_확인() {
        // given
        SignUpDto user1 = TestDto.getSignUpDto();
        SignUpDto user2 = TestDto.getSignUpDto();
        user2.setId("test2");

        // when
        ownerMapper.save(user1);
        ownerMapper.save(user2);

        //then
        assertThat(ownerMapper.existById(user1.getId())).isTrue();
        assertThat(ownerMapper.existById(user2.getId())).isTrue();
    }

    @Test
    void countOwner_확인() {
        // given
        SignUpDto user1 = TestDto.getSignUpDto();
        SignUpDto user2 = TestDto.getSignUpDto();
        user2.setId("test2");

        // when
        ownerMapper.save(user1);
        ownerMapper.save(user2);

        //then
        assertThat(ownerMapper.countById()).isEqualTo(2);
    }
}