package com.flab.delivery.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.UserDto;
import com.flab.delivery.enums.UserLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
                            .andExpect(content().string("비밀번호는 필수 입력값입니다"))
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
                            .andExpect(content().string("비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요."))
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

}