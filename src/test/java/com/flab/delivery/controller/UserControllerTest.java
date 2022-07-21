package com.flab.delivery.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.delivery.annotation.EnableMockMvc;
import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.UserDto;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.mapper.UserMapper;
import com.flab.delivery.utils.SessionConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@Transactional
@EnableMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    private static final String NOT_EXISTS_SESSION_MESSAGE = "세션 아이디가 존재하지 않습니다";
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserMapper userMapper;

    MockHttpSession mockHttpSession = new MockHttpSession();

    @Nested
    @DisplayName("POST : /users")
    class createUser {
        private String id = "testUser1";
        private String password = "!Test1111";
        private String email = "user@naver.com";
        private String name = "유저1";
        private String phoneNumber = "010-1111-1111";
        private UserType level = UserType.USER;

        private SignUpDto getSignUpDto() {
            return SignUpDto.builder()
                    .id(id)
                    .password(password)
                    .name(name)
                    .email(email)
                    .phoneNumber(phoneNumber)
                    .type(level)
                    .build();
        }

        @Nested
        @DisplayName("성공")
        class SuccessCase {

            @Test
            @DisplayName("회원가입")
            void crateUser_success() throws Exception {
                SignUpDto userDto = getSignUpDto();
                String json = objectMapper.writeValueAsString(userDto);
                mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(jsonPath("$.status").value(201))
                        .andExpect(jsonPath("$.message").value("요청 성공하였습니다."))
                        .andDo(print());
            }
        }


        @Nested
        @DisplayName("실패")
        class FailCase {

            @Nested
            @DisplayName("아이디")
            class IdCase {

                @Test
                @DisplayName("NULL")
                void id_NULL() throws Exception {
                    id = null;
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("아이디 길이는 4~20자 입니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("공백")
                void id_empty() throws Exception {
                    id = "";
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("아이디 길이는 4~20자 입니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("중복")
                void id_overlap() throws Exception {
                    id = "user1";
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("이미 존재하는 아이디입니다"))
                            .andDo(print());
                }


            }

            @Nested
            @DisplayName("비밀번호")
            class PasswordCase {

                @Test
                @DisplayName("NULL")
                void password_null() throws Exception {
                    password = null;
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요"))
                            .andDo(print());
                }

                @Test
                @DisplayName("공백")
                void password_empty() throws Exception {
                    password = "";
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요"))
                            .andDo(print());
                }

                @Test
                @DisplayName("길이")
                void password_length() throws Exception {
                    password = "1111";
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요"))
                            .andDo(print());
                }

                @Test
                @DisplayName("대소문자")
                void password_form() throws Exception {
                    password = "@11111111";
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요"))
                            .andDo(print());
                }

            }


            @Nested
            @DisplayName("이메일")
            class EmailCase {

                @Test
                @DisplayName("NULL")
                void email_null() throws Exception {
                    email = null;
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("이메일은 필수 입력값입니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("공백")
                void email_empty() throws Exception {
                    email = "";
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("이메일 형식이 아닙니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("형식")
                void email_form() throws Exception {
                    email = "user!naver.com";
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("이메일 형식이 아닙니다"))
                            .andDo(print());
                }
            }

            @Nested
            @DisplayName("이름")
            class NameCase {

                @Test
                @DisplayName("NULL")
                void name_null() throws Exception {
                    name = null;
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("이름은 필수 입력값입니다"))
                            .andDo(print());

                }

                @Test
                @DisplayName("공백")
                void name_empty() throws Exception {
                    name = "";
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("이름은 필수 입력값입니다"))
                            .andDo(print());
                }

            }

            @Nested
            @DisplayName("핸드폰번호")
            class PhoneNumberCase {

                @Test
                @DisplayName("NULL")
                void phoneNumber_null() throws Exception {
                    phoneNumber = null;
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("핸드폰 번호는 필수 입력값입니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("공백")
                void phoneNumber_empty() throws Exception {
                    phoneNumber = "";
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("핸드폰 형식이 아닙니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("형식")
                void phoneNumber_form() throws Exception {
                    phoneNumber = "01011111111";
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("핸드폰 형식이 아닙니다"))
                            .andDo(print());
                }
            }

            @Nested
            @DisplayName("레벨")
            class LevelCase {

                @Test
                @DisplayName("NULL")
                void level_null() throws Exception {
                    level = null;
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("권한은 필수 입력값입니다"))
                            .andDo(print());
                }

            }

        }

    }


    @Nested
    @DisplayName("/users/login")
    class loginUser {
        private String id = "user1";
        private String password = "1111";

        private UserDto getUserDto() {
            return UserDto.builder()
                    .id(id)
                    .password(password)
                    .build();
        }

        @Nested
        @DisplayName("성공")
        class SuccessCase {

            @Test
            @DisplayName("로그인 성공")
            void loginUser_success() throws Exception {
                UserDto userDto = getUserDto();
                String json = objectMapper.writeValueAsString(userDto);
                mockMvc.perform(post("/users/login").contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                        .andDo(print());

            }

        }

        @Nested
        @DisplayName("실패")
        class FailCase {
            @Test
            @DisplayName("아이디 존재x 경우")
            void isExistsId() throws Exception {
                String diffId = "diffUser";
                id = diffId;
                UserDto userDto = getUserDto();
                String json = objectMapper.writeValueAsString(userDto);
                mockMvc.perform(post("/users/login").contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                        .andExpect(jsonPath("$.message").value("존재하지 않는 아이디입니다"))
                        .andDo(print());

            }


            @Test
            @DisplayName("비밀번호 틀리는경우")
            void password() throws Exception {
                String diffPassword = "2222";
                password = diffPassword;
                UserDto userDto = getUserDto();
                String json = objectMapper.writeValueAsString(userDto);
                mockMvc.perform(post("/users/login").contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                        .andExpect(jsonPath("$.message").value("아이디랑 비밀번호를 확인해주세요"))
                        .andDo(print());
            }
        }
    }


    @Nested
    @DisplayName("/users/logout")
    class logoutUser {
        MockHttpSession mockHttpSession = new MockHttpSession();

        @Nested
        @DisplayName("성공")
        class SuccessCase {
            @Test
            @DisplayName("로그 아웃")
            void logout_success() throws Exception {
                mockHttpSession.setAttribute(SessionConstants.SESSION_ID, "user1");
                mockMvc.perform(delete("/users/logout").session(mockHttpSession))
                        .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                        .andDo(print());

                Assertions.assertNull(mockHttpSession.getAttribute(SessionConstants.SESSION_ID));
            }
        }

        @Nested
        @DisplayName("실패")
        class FailCase {

            @Test
            @DisplayName("세션 없는 경우")
            void session() throws Exception {
                mockMvc.perform(delete("/users/logout"))
                        .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                        .andExpect(jsonPath("$.message").value(NOT_EXISTS_SESSION_MESSAGE))
                        .andDo(print());

            }
        }


    }

    @Test
    void getUserInfo_성공() throws Exception {
        // given
        String user = "user1";
        mockHttpSession.setAttribute(SessionConstants.SESSION_ID, user);
        mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, UserType.USER);
        UserDto findUser = userMapper.findById(user);

        // when
        // then
        mockMvc.perform(get("/users").session(mockHttpSession))
                .andExpect(jsonPath("$.message").value("요청 성공하였습니다."))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.email").value(findUser.getEmail()))
                .andExpect(jsonPath("$.data.name").value(findUser.getName()))
                .andExpect(jsonPath("$.data.phoneNumber").value(findUser.getPhoneNumber()))
                .andExpect(jsonPath("$.data.type").value(findUser.getType().name()))
                .andExpect(jsonPath("$.data.createdAt").value(findUser.getCreatedAt().toString()))
                .andExpect(jsonPath("$.data.modifiedAt").value(findUser.getModifiedAt().toString()));

    }

    @Test
    void getUserInfo_로그인하지_않은_유저_실패() throws Exception {
        // given
        String user = "user1";

        // when
        // then
        mockMvc.perform(get("/users").session(mockHttpSession))
                .andExpect(jsonPath("$.message").value(NOT_EXISTS_SESSION_MESSAGE))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());
    }
}