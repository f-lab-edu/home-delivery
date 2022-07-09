package com.flab.delivery.service;

import com.flab.delivery.dto.UserDto;
import com.flab.delivery.enums.UserLevel;
import com.flab.delivery.exception.SignUpException;
import com.flab.delivery.mapper.UserMapper;
import com.flab.delivery.utils.PasswordEncoder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserServiceImpl userService;


    @Nested
    @DisplayName("회원가입")
    class createUser{

        private UserDto userDto;

        @BeforeEach
        void setUp(){
            userDto = UserDto.builder()
                    .id("user1")
                    .password(PasswordEncoder.encrypt("1111"))
                    .email("user@naver.com")
                    .name("유저1")
                    .phoneNumber("010-1111-111")
                    .level(UserLevel.USER)
                    .build();
        }


        @Nested
        @DisplayName("성공 케이스")
        class SuccessCase {
            @Test
            @DisplayName("회원가입 성공")
            void createUser_success(){
                // given
                when(userMapper.isExistsId("user1")).thenReturn(false);
                when(userMapper.findById("user1")).thenReturn(userDto);
                // when
                userService.createUser(userDto);
                UserDto findUser = userMapper.findById("user1");
                // then
                Assertions.assertEquals("user1", findUser.getId());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCase {
            @Test
            @DisplayName("아이디 이미 존재")
            void createUser_fail(){
                // given
                when(userMapper.isExistsId("user1")).thenReturn(true);
                // when
                SignUpException e = Assertions.assertThrows(SignUpException.class, () -> {
                    userService.createUser(userDto);
                });
                // then
                Assertions.assertEquals(e.getMessage(), "이미 존재하는 아이디입니다");
            }
        }
    }


}