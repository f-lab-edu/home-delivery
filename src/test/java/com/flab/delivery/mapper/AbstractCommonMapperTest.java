package com.flab.delivery.mapper;

import com.flab.delivery.dto.MemberDto;
import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.TestDto;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.dao.DataAccessException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@MybatisTest
public abstract class AbstractCommonMapperTest {
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
        assertThat(mapper.existsById(signUpDto.getId())).isTrue();
    }

    @Test
    void save_이미_존재하는_아이디로_인한_실패() {

        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();

        // when
        mapper.save(signUpDto);
        assertThatThrownBy(() -> mapper.save(signUpDto)).isInstanceOf(DataAccessException.class);

        //then
        assertThat(mapper.existsById(signUpDto.getId())).isTrue();
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
        assertThat(mapper.existsById(user1.getId())).isTrue();
        assertThat(mapper.existsById(user2.getId())).isTrue();
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
        MemberDto memberDto = mapper.findMemberById(user1.getId()).get();

        //then
        assertEqual(memberDto.getId(), user1.getId());
        assertEqual(memberDto.getName(), user1.getName());
        assertEqual(memberDto.getPassword(), user1.getPassword());
        assertEqual(memberDto.getPhoneNumber(), user1.getPhoneNumber());
        assertEqual(memberDto.getEmail(), user1.getEmail());
    }

    @Test
    void findMemberById_존재하지_않음() {
        // given
        // when
        Optional<MemberDto> member = mapper.findMemberById("noId");

        //then
        assertEqual(member, Optional.empty());
    }

    private ObjectAssert<Object> assertEqual(Object arg1, Object arg2) {
        return assertThat(arg1).isEqualTo(arg2);
    }
}
