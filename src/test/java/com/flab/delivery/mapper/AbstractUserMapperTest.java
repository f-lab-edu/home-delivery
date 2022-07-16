package com.flab.delivery.mapper;

import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.TestDto;
import com.flab.delivery.dto.UserDto;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@MybatisTest
public class AbstractUserMapperTest {

    @Autowired
    UserMapper mapper;


    @Test
    void save_확인() {

        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();

        // when
        mapper.save(signUpDto);

        //then
        assertThat(mapper.hasUserById(signUpDto.getId())).isTrue();
    }

    @Test
    void save_이미_존재하는_아이디로_인한_실패() {

        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();

        // when
        mapper.save(signUpDto);
        assertThatThrownBy(() -> mapper.save(signUpDto)).isInstanceOf(DataAccessException.class);

        //then
        assertThat(mapper.hasUserById(signUpDto.getId())).isTrue();
        assertThat(mapper.getCountById()).isEqualTo(1);
    }

    @Test
    void existsById_확인() {
        // given
        SignUpDto user1 = TestDto.getSignUpDto();
        SignUpDto user2 = TestDto.getSignUpDto();
        user2.setId("test2");

        // when
        mapper.save(user1);
        mapper.save(user2);

        //then
        assertThat(mapper.hasUserById(user1.getId())).isTrue();
        assertThat(mapper.hasUserById(user2.getId())).isTrue();
    }

    @Test
    void getCountById_확인() {
        // given
        SignUpDto user1 = TestDto.getSignUpDto();
        SignUpDto user2 = TestDto.getSignUpDto();
        user2.setId("test2");

        // when
        mapper.save(user1);
        mapper.save(user2);

        //then
        assertThat(mapper.getCountById()).isEqualTo(2);
    }

    @Test
    void findMemberById_존재() {
        // given
        SignUpDto user1 = TestDto.getSignUpDto();
        mapper.save(user1);

        // when
        UserDto userDto = mapper.findUserById(user1.getId()).get();

        //then
        assertEqual(userDto.getId(), user1.getId());
        assertEqual(userDto.getName(), user1.getName());
        assertEqual(userDto.getPassword(), user1.getPassword());
        assertEqual(userDto.getPhoneNumber(), user1.getPhoneNumber());
        assertEqual(userDto.getEmail(), user1.getEmail());
    }

    @Test
    void findMemberById_존재하지_않음() {
        // given
        // when
        Optional<UserDto> member = mapper.findUserById("noId");

        //then
        assertEqual(member, Optional.empty());
    }

    private ObjectAssert<Object> assertEqual(Object arg1, Object arg2) {
        return assertThat(arg1).isEqualTo(arg2);
    }
}
