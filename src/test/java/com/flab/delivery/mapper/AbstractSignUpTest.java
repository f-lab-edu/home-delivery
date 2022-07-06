package com.flab.delivery.mapper;

import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.TestDto;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.dao.DataAccessException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@MybatisTest
public abstract class AbstractSignUpTest {
    CommonMapper mapper;

    public void setMapper(CommonMapper mapper) {
        this.mapper = mapper;
    }

    @Test
    void save_확인() {

        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();

        // when
        mapper.save(signUpDto);

        //then
        assertThat(mapper.existById(signUpDto.getId())).isTrue();
    }

    @Test
    void save_이미_존재하는_아이디로_인한_실패() {

        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();

        // when
        mapper.save(signUpDto);
        assertThatThrownBy(() -> mapper.save(signUpDto)).isInstanceOf(DataAccessException.class);

        //then
        assertThat(mapper.existById(signUpDto.getId())).isTrue();
        assertThat(mapper.countById()).isEqualTo(1);
    }

    @Test
    void existUserById_확인() {
        // given
        SignUpDto user1 = TestDto.getSignUpDto();
        SignUpDto user2 = TestDto.getSignUpDto();
        user2.setId("test2");

        // when
        mapper.save(user1);
        mapper.save(user2);

        //then
        assertThat(mapper.existById(user1.getId())).isTrue();
        assertThat(mapper.existById(user2.getId())).isTrue();
    }

    @Test
    void countUser_확인() {
        // given
        SignUpDto user1 = TestDto.getSignUpDto();
        SignUpDto user2 = TestDto.getSignUpDto();
        user2.setId("test2");

        // when
        mapper.save(user1);
        mapper.save(user2);

        //then
        assertThat(mapper.countById()).isEqualTo(2);
    }
}
