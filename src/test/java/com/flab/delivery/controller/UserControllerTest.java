package com.flab.delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.delivery.dto.LoginDto;
import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.TestDto;
import com.flab.delivery.mapper.UserMapper;
import com.flab.delivery.service.LoginService;
import com.flab.delivery.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.flab.delivery.utils.Constants.SESSION_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserMapper mapper;
    @Autowired
    UserService userService;

    @Autowired
    LoginService loginService;

    MockHttpSession mockHttpSession = new MockHttpSession();
    private final String uri = "/users";

    @Test
    void signUp_성공() throws Exception {
        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();

        // when
        mockMvc.perform(post(uri)
                        .content(objectMapper.writeValueAsString(signUpDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").exists());

        //then
        assertThat(mapper.hasUserById(signUpDto.getId())).isTrue();
    }

    @Test
    void signUp_파라미터_검증에_잡혀_실패() throws Exception {

        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();
        signUpDto.setPassword("asd1");

        // when
        mockMvc.perform(post(uri)
                        .content(objectMapper.writeValueAsString(signUpDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists())
                .andDo(print());

        //then
        assertThat(mapper.hasUserById(signUpDto.getId())).isFalse();

    }

    @Test
    void existById_중복되는_아이디_없어서_성공() throws Exception {

        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();

        // when
        // then
        mockMvc.perform(get(getExistsUri(signUpDto)))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").exists())
                .andDo(print());


    }

    @Test
    void existById_이미_존재하는_아이디로_실패() throws Exception {

        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();
        mapper.save(signUpDto);

        // when
        // then
        mockMvc.perform(get(getExistsUri(signUpDto)))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists())
                .andDo(print());


    }

    private String getExistsUri(SignUpDto signUpDto) {
        return uri + "/" + signUpDto.getId() + "/exists";
    }

    @Test
    void login_성공() throws Exception {
        // given
        LoginDto loginDto = TestDto.getLoginDto();
        userService.signUp(TestDto.getSignUpDto());

        // when
        // then
        mockMvc.perform(post(uri + "/login")
                        .content(objectMapper.writeValueAsString(loginDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void login_존재하지_않는_아이디_실패() throws Exception {
        // given
        LoginDto loginDto = TestDto.getLoginDto();

        // when
        // then
        mockMvc.perform(post(uri + "/login")
                        .content(objectMapper.writeValueAsString(loginDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void login_비밀번호가_틀려서_실패() throws Exception {
        // given
        LoginDto loginDto = TestDto.getLoginDto();
        userService.signUp(TestDto.getSignUpDto());
        loginDto.setPassword("WrongPassword");

        // when
        // then
        mockMvc.perform(post(uri + "/login")
                        .content(objectMapper.writeValueAsString(loginDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void logout_성공() throws Exception {
        // given
        userService.signUp(TestDto.getSignUpDto());
        mockHttpSession.setAttribute(SESSION_ID, "test");

        // when
        mockMvc.perform(delete(uri + "/logout")
                        .session(mockHttpSession))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").exists());

        //then
        assertThat(mockHttpSession.getAttribute(SESSION_ID)).isNull();
    }

    @Test
    void logout_로그인하지_않은_회원_실패() throws Exception {
        // given
        // when
        //then
        mockMvc.perform(delete(uri + "/logout")
                        .session(mockHttpSession))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").exists());

    }
}
