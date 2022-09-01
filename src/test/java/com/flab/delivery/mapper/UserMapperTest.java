package com.flab.delivery.mapper;

import com.flab.delivery.AbstractDockerContainer;
import com.flab.delivery.config.DatabaseConfig;
import com.flab.delivery.dto.user.PasswordDto;
import com.flab.delivery.dto.user.UserDto;
import com.flab.delivery.dto.user.UserInfoUpdateDto;
import com.flab.delivery.utils.PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MybatisTest
@Import({DatabaseConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserMapperTest extends AbstractDockerContainer {

    @Autowired
    private UserMapper userMapper;

    @Test
    void updateInfo_확인() {
        // given
        UserDto findUser = userMapper.findById("user1");

        UserInfoUpdateDto userInfoRequestDto = UserInfoUpdateDto.builder()
                .id(findUser.getId())
                .name("유저2")
                .phoneNumber("010-1234-1234")
                .email("user2@naver.com")
                .build();

        // when
        int changedRow = userMapper.updateInfo(userInfoRequestDto);

        //then
        assertEquals(changedRow, 1);

        UserDto updateUser = userMapper.findById("user1");

        assertThat(updateUser.getModifiedAt()).isAfter(findUser.getModifiedAt());
        assertThat(updateUser.getEmail()).isEqualTo(userInfoRequestDto.getEmail());
        assertThat(updateUser.getName()).isEqualTo(userInfoRequestDto.getName());
        assertThat(updateUser.getPhoneNumber()).isEqualTo(userInfoRequestDto.getPhoneNumber());
        assertThat(updateUser.getId()).isEqualTo(userInfoRequestDto.getId());
        assertThat(updateUser.getPassword()).isEqualTo(findUser.getPassword());
    }

    @Test
    void deleteById_확인() {
        // given
        UserDto findUser = userMapper.findById("user1");

        // when
        userMapper.deleteById(findUser.getId());

        // then
        boolean idExists = userMapper.idExists(findUser.getId());
        assertThat(idExists).isFalse();
    }

    @Test
    void changePassword_확인() {
        // given
        UserDto findUser = userMapper.findById("user1");
        PasswordDto passwordDto = PasswordDto.builder()
                .password("password")
                .newPassword("!newPassword")
                .build();

        // when
        userMapper.changePassword(findUser.getId(), PasswordEncoder.encrypt(passwordDto.getNewPassword()));

        //then
        UserDto updateUser = userMapper.findById("user1");
        assertThat(updateUser.getPassword()).isNotEqualTo(findUser.getPassword());
    }
}