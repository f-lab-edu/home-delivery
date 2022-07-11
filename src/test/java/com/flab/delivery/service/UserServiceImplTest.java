package com.flab.delivery.service;

import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.UserDto;
import com.flab.delivery.enums.UserLevel;
import com.flab.delivery.exception.LoginException;
import com.flab.delivery.exception.SignUpException;
import com.flab.delivery.mapper.UserMapper;
import com.flab.delivery.utils.PasswordEncoder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    HttpSession httpSession;


    @Nested
    @DisplayName("회원가입")
    class createUser{

        private SignUpDto signUpDto;

        @BeforeEach
        void setUp(){
            signUpDto = SignUpDto.builder()
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
                UserDto userDto = UserDto.builder()
                        .id("user1")
                        .build();

                when(userMapper.isExistsId("user1")).thenReturn(false);
                when(userMapper.findById("user1")).thenReturn(userDto);
                // when
                userService.createUser(signUpDto);
                UserDto findUser = userMapper.findById("user1");
                // then
                Assertions.assertEquals(signUpDto.getId(), findUser.getId());
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
                    userService.createUser(signUpDto);
                });
                // then
                Assertions.assertEquals(e.getMessage(), "이미 존재하는 아이디입니다");
            }
        }
    }

    @Nested
    @DisplayName("로그인")
    class loginUser{
        private UserDto userDto;
        @BeforeEach
        void setUp(){
            userDto = UserDto.builder()
                    .id("user1")
                    .password("1111")
                    .build();
        }

        @Nested
        @DisplayName("성공")
        class SuccessCase{

            @Test
            @DisplayName("로그인 성공")
            void loginUser_success(){
                // given
                when(userMapper.isExistsId(userDto.getId())).thenReturn(true);
                when(userMapper.findById(userDto.getId())).thenReturn(UserDto.builder()
                                .id(userDto.getId())
                                .password(PasswordEncoder.encrypt(userDto.getPassword()))
                        .build());
                when(httpSession.getAttribute("SESSION_ID")).thenReturn("user1");
                // when
                userService.loginUser(userDto);
                String getSessionID = (String)httpSession.getAttribute("SESSION_ID");
                // then
                Assertions.assertEquals(getSessionID, "user1");
            }
        }

        @Nested
        @DisplayName("실패")
        class FailCase{

            @Test
            @DisplayName("아이디 존재x 경우")
            void loginUser_fail_아이디(){
                // given
                when(userMapper.isExistsId("user1")).thenReturn(false);
                // when
                LoginException ex = Assertions.assertThrows(LoginException.class, () -> {
                    userService.loginUser(userDto);
                });
                // then
                Assertions.assertEquals(ex.getMessage(), "존재하지 않는 아이디입니다");
            }

            @Test
            @DisplayName("비밀번호 틀린경우")
            void loginUser_fail_패스워드(){
                // given
                String diffPassword = "2222";

                when(userMapper.isExistsId(userDto.getId())).thenReturn(true);
                when(userMapper.findById(userDto.getId())).thenReturn(UserDto.builder()
                        .id(userDto.getId())
                        .password(PasswordEncoder.encrypt(diffPassword))
                        .build());
                // when
                LoginException ex = Assertions.assertThrows(LoginException.class, () -> {
                    userService.loginUser(userDto);
                });
                // then
                Assertions.assertEquals(ex.getMessage(), "아이디랑 비밀번호를 확인해주세요");
            }
        }

    }


}