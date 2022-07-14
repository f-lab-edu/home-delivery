package com.flab.delivery.controller;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.UserDto;
import com.flab.delivery.enums.UserLevel;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;



    @Nested
    @DisplayName("/users/signup")
    class createUser{
        private String id = "testUser1";
        private String password = "!Test1111";
        private String email = "user@naver.com";
        private String name = "유저1";
        private String phoneNumber = "010-1111-1111";
        private UserLevel level = UserLevel.USER;

        private SignUpDto getSignUpDto(){
            return SignUpDto.builder()
                    .id(id)
                    .password(password)
                    .name(name)
                    .email(email)
                    .phoneNumber(phoneNumber)
                    .level(level)
                    .build();
        }

        @Nested
        @DisplayName("성공")
        class SuccessCase{

            @Test
            @DisplayName("회원가입")
            void crateUser_success() throws Exception{
                SignUpDto userDto = getSignUpDto();
                String json = objectMapper.writeValueAsString(userDto);
                mockMvc.perform(post("/users/signup").contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(status().is2xxSuccessful())
                        .andDo(print());
            }
        }


        @Nested
        @DisplayName("실패")
        class FailCase{

            @Nested
            @DisplayName("아이디")
            class IdCase{

                @Test
                @DisplayName("NULL")
                void id_NULL() throws Exception {
                    id = null;
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users/signup").contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                            .andExpect(status().isBadRequest())
                            .andExpect(content().string("아이디는 필수 입력값입니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("공백")
                void id_empty() throws Exception{
                    id = "";
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users/signup").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(status().isBadRequest())
                            .andExpect(content().string("아이디는 필수 입력값입니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("중복")
                void id_overlap() throws Exception{
                    id = "user1";
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users/signup").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(status().isBadRequest())
                            .andExpect(content().string("이미 존재하는 아이디입니다"))
                            .andDo(print());
                }


            }

            @Nested
            @DisplayName("비밀번호")
            class PasswordCase{

                @Test
                @DisplayName("NULL")
                void password_null() throws Exception{
                    password = null;
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users/signup").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(status().isBadRequest())
                            .andExpect(content().string("비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요"))
                            .andDo(print());
                }

                @Test
                @DisplayName("공백")
                void password_empty() throws Exception{
                    password = "";
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users/signup").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(status().isBadRequest())
                            .andExpect(content().string("비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요"))
                            .andDo(print());
                }

                @Test
                @DisplayName("길이")
                void password_length() throws Exception{
                    password = "1111";
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users/signup").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(status().isBadRequest())
                            .andExpect(content().string("비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요"))
                            .andDo(print());
                }

                @Test
                @DisplayName("대소문자")
                void password_form() throws Exception{
                    password = "@11111111";
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users/signup").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(status().isBadRequest())
                            .andExpect(content().string("비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요"))
                            .andDo(print());
                }

            }


            @Nested
            @DisplayName("이메일")
            class EmailCase{

                @Test
                @DisplayName("NULL")
                void email_null() throws Exception{
                    email = null;
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users/signup").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(status().isBadRequest())
                            .andExpect(content().string("이메일은 필수 입력값입니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("공백")
                void email_empty() throws Exception{
                    email = "";
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users/signup").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(status().isBadRequest())
                            .andExpect(content().string("이메일 형식이 아닙니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("형식")
                void email_form() throws Exception{
                    email = "user!naver.com";
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users/signup").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(status().isBadRequest())
                            .andExpect(content().string("이메일 형식이 아닙니다"))
                            .andDo(print());
                }
            }

            @Nested
            @DisplayName("이름")
            class NameCase{

                @Test
                @DisplayName("NULL")
                void name_null() throws Exception{
                    name = null;
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users/signup").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(status().isBadRequest())
                            .andExpect(content().string("이름은 필수 입력값입니다"))
                            .andDo(print());

                }

                @Test
                @DisplayName("공백")
                void name_empty() throws Exception{
                    name = "";
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users/signup").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(status().isBadRequest())
                            .andExpect(content().string("이름은 필수 입력값입니다"))
                            .andDo(print());
                }

            }

            @Nested
            @DisplayName("핸드폰번호")
            class PhoneNumberCase{

                @Test
                @DisplayName("NULL")
                void phoneNumber_null() throws Exception{
                    phoneNumber = null;
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users/signup").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(status().isBadRequest())
                            .andExpect(content().string("핸드폰 번호는 필수 입력값입니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("공백")
                void phoneNumber_empty() throws Exception{
                    phoneNumber = "";
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users/signup").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(status().isBadRequest())
                            .andExpect(content().string("핸드폰 형식이 아닙니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("형식")
                void phoneNumber_form() throws Exception{
                    phoneNumber = "01011111111";
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users/signup").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(status().isBadRequest())
                            .andExpect(content().string("핸드폰 형식이 아닙니다"))
                            .andDo(print());

                }
            }

            @Nested
            @DisplayName("레벨")
            class LevelCase{

                @Test
                @DisplayName("NULL")
                void level_null() throws Exception{
                    level = null;
                    SignUpDto userDto = getSignUpDto();
                    String json = objectMapper.writeValueAsString(userDto);
                    mockMvc.perform(post("/users/signup").contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(status().isBadRequest())
                            .andExpect(content().string("권한은 필수 입력값입니다"))
                            .andDo(print());
                }

            }

        }

    }


    @Nested
    @DisplayName("/users/login")
    class loginUser{
        private String id = "user1";
        private String password = "1111";

        private UserDto getUserDto(){
            return UserDto.builder()
                    .id(id)
                    .password(password)
                    .build();
        }

        @Nested
        @DisplayName("성공")
        class SuccessCase{

            @Test
            @DisplayName("로그인 성공")
            void loginUser_success() throws Exception{
                UserDto userDto = getUserDto();
                String json = objectMapper.writeValueAsString(userDto);
                mockMvc.perform(post("/users/login").contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(status().is2xxSuccessful())
                        .andDo(print());

            }

        }

        @Nested
        @DisplayName("실패")
        class FailCase{
            @Test
            @DisplayName("아이디 존재x 경우")
            void isExistsId() throws Exception{
                String diffId = "diffUser";
                id = diffId;
                UserDto userDto = getUserDto();
                String json = objectMapper.writeValueAsString(userDto);
                mockMvc.perform(post("/users/login").contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(status().is4xxClientError())
                        .andExpect(content().string("존재하지 않는 아이디입니다"))
                        .andDo(print());

            }


            @Test
            @DisplayName("비밀번호 틀리는경우")
            void password() throws Exception{
                String diffPassword = "2222";
                password = diffPassword;
                UserDto userDto = getUserDto();
                String json = objectMapper.writeValueAsString(userDto);
                mockMvc.perform(post("/users/login").contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(status().is4xxClientError())
                        .andExpect(content().string("아이디랑 비밀번호를 확인해주세요"))
                        .andDo(print());
            }
        }
    }


    @Nested
    @DisplayName("/users/logout")
    class logoutUser{
        MockHttpSession mockHttpSession = new MockHttpSession();

        @Nested
        @DisplayName("성공")
        class SuccessCase{
            @Test
            @DisplayName("로그 아웃")
            void logout_success() throws Exception {
                mockHttpSession.setAttribute("SESSION_ID", "user1");
                mockMvc.perform(delete("/users/logout").session(mockHttpSession))
                        .andExpect(status().is2xxSuccessful())
                        .andDo(print());

                Assertions.assertNull(mockHttpSession.getAttribute("SESSION_ID"));
            }
        }

        @Nested
        @DisplayName("실패")
        class FailCase{

            @Test
            @DisplayName("세션 없는 경우")
            void session() throws Exception {
                mockMvc.perform(delete("/users/logout"))
                        .andExpect(status().is4xxClientError())
                        .andExpect(content().string("세션 아이디가 존재하지 않습니다"))
                        .andDo(print());

            }
        }


    }
}