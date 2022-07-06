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
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void save_확인() {

        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();

        // when
        userMapper.save(signUpDto);

        //then
        assertThat(userMapper.existUserById(signUpDto.getId())).isTrue();
    }

    @Test
    void save_이미_존재하는_PK로_인한_실패() {

        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();

        // when
        userMapper.save(signUpDto);
        assertThatThrownBy(() -> userMapper.save(signUpDto)).isInstanceOf(DataAccessException.class);

        //then
        assertThat(userMapper.existUserById(signUpDto.getId())).isTrue();
        assertThat(userMapper.countUser()).isEqualTo(1);
    }

    @Test
    void existUserById_확인() {
        // given
        SignUpDto user1 = TestDto.getSignUpDto();
        SignUpDto user2 = TestDto.getSignUpDto();
        user2.setId("test2");

        // when
        userMapper.save(user1);
        userMapper.save(user2);

        //then
        assertThat(userMapper.existUserById(user1.getId())).isTrue();
        assertThat(userMapper.existUserById(user2.getId())).isTrue();
    }

    @Test
    void countUser_확인() {
        // given
        SignUpDto user1 = TestDto.getSignUpDto();
        SignUpDto user2 = TestDto.getSignUpDto();
        user2.setId("test2");

        // when
        userMapper.save(user1);
        userMapper.save(user2);

        //then
        assertThat(userMapper.countUser()).isEqualTo(2);
    }

}