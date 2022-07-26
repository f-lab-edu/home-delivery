package com.flab.delivery.service;

import com.flab.delivery.dto.user.*;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.exception.LoginException;
import com.flab.delivery.exception.SessionLoginException;
import com.flab.delivery.exception.SignUpException;
import com.flab.delivery.fixture.TestDto;
import com.flab.delivery.mapper.UserMapper;
import com.flab.delivery.utils.PasswordEncoder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserService userService;

    @Mock
    SessionLoginService loginService;


    @Nested
    @DisplayName("회원가입")
    class createUser {

        private SignUpDto signUpDto;

        @BeforeEach
        void setUp() {
            signUpDto = SignUpDto.builder()
                    .id("user1")
                    .password(PasswordEncoder.encrypt("1111"))
                    .email("user@naver.com")
                    .name("유저1")
                    .phoneNumber("010-1111-111")
                    .type(UserType.USER)
                    .build();
        }


        @Nested
        @DisplayName("성공")
        class SuccessCase {
            @Test
            @DisplayName("회원가입 성공")
            void createUser_success() {
                // given
                UserDto userDto = UserDto.builder()
                        .id("user1")
                        .build();

                when(userMapper.idExists("user1")).thenReturn(false);
                when(userMapper.findById("user1")).thenReturn(userDto);
                // when
                userService.createUser(signUpDto);
                UserDto findUser = userMapper.findById("user1");
                // then
                Assertions.assertEquals(signUpDto.getId(), findUser.getId());
            }

        }

        @Nested
        @DisplayName("실패")
        class FailCase {
            @Test
            @DisplayName("아이디 이미 존재")
            void createUser_fail() {
                // given
                when(userMapper.idExists("user1")).thenReturn(true);
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
    class loginUser {
        private UserDto userDto;

        @BeforeEach
        void setUp() {
            userDto = UserDto.builder()
                    .id("user1")
                    .password("1111")
                    .build();
        }

        @Nested
        @DisplayName("성공")
        class SuccessCase {

            @Test
            @DisplayName("로그인 성공")
            void loginUser_success() {
                // given
                when(userMapper.idExists(userDto.getId())).thenReturn(true);
                when(userMapper.findById(userDto.getId())).thenReturn(UserDto.builder()
                        .id(userDto.getId())
                        .password(PasswordEncoder.encrypt(userDto.getPassword()))
                        .build());

                when(loginService.getSessionUserId()).thenReturn("user1");
                // when
                userService.loginUser(userDto);
                String getSessionID = loginService.getSessionUserId();
                // then
                Assertions.assertEquals(getSessionID, "user1");
            }
        }

        @Nested
        @DisplayName("실패")
        class FailCase {

            @Test
            @DisplayName("아이디 존재x 경우")
            void loginUser_fail_아이디() {
                // given
                when(userMapper.idExists("user1")).thenReturn(false);
                // when
                LoginException ex = Assertions.assertThrows(LoginException.class, () -> {
                    userService.loginUser(userDto);
                });
                // then
                Assertions.assertEquals(ex.getMessage(), "존재하지 않는 아이디입니다");
            }

            @Test
            @DisplayName("비밀번호 틀린경우")
            void loginUser_fail_패스워드() {
                // given
                String diffPassword = "2222";

                when(userMapper.idExists(userDto.getId())).thenReturn(true);
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


    @Test
    void getUserInfo_성공() {
        // given
        UserDto userDto = TestDto.getUserDto();
        when(userMapper.findById(eq("user1"))).thenReturn(userDto);

        // when
        UserInfoDto getUserInfo = userService.getUserInfo("user1");

        //then
        assertThat(getUserInfo.getName()).isEqualTo(userDto.getName());
        assertThat(getUserInfo.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(getUserInfo.getPhoneNumber()).isEqualTo(userDto.getPhoneNumber());
        assertThat(getUserInfo.getCreatedAt()).isEqualTo(userDto.getCreatedAt());
        assertThat(getUserInfo.getModifiedAt()).isEqualTo(userDto.getModifiedAt());
    }


    @Test
    void updateUserInfo_동일하지_않은_회원_실패() {
        // given
        UserInfoUpdateDto infoUpdateDto = UserInfoUpdateDto.builder()
                .id("wrongId")
                .name("테스트2")
                .phoneNumber("010-1234-1234")
                .email("test@naver.com")
                .build();

        // when
        assertThatThrownBy(() -> userService.updateUserInfo("user1", infoUpdateDto))
                .isInstanceOf(SessionLoginException.class);

        //then
        verify(userMapper, never()).updateInfo(any());
    }

    @Test
    void updateUserInfo_성공() {
        // given
        UserInfoUpdateDto infoUpdateDto = TestDto.getUserInfoUpdateDto();

        // when
        userService.updateUserInfo("user1", infoUpdateDto);

        //then
        verify(userMapper).updateInfo(any());
    }

    @Test
    void changePassword_성공() {
        // given
        PasswordDto passwordDto = TestDto.getPasswordDto();

        // when
        userService.changePassword("user1", passwordDto);

        //then
        verify(userMapper).changePassword(eq("user1"), any());
    }
}