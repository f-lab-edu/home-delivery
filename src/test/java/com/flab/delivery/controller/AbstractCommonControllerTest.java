package com.flab.delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.delivery.dto.LoginDto;
import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.TestDto;
import com.flab.delivery.mapper.CommonMapper;
import com.flab.delivery.service.CommonService;
import com.flab.delivery.service.LoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public abstract class AbstractCommonControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    CommonMapper mapper;
    @Autowired
    CommonService commonService;

    @Autowired
    LoginService loginService;

    String uri;

    public void setMapper(CommonMapper mapper) {
        this.mapper = mapper;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Test
    void signUp_성공() throws Exception {
        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();

        // when
        mockMvc.perform(post(uri)
                        .content(objectMapper.writeValueAsString(signUpDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        //then
        assertThat(mapper.existsById(signUpDto.getId())).isTrue();
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
                .andExpect(status().isBadRequest())
                .andDo(print());

        //then
        assertThat(mapper.existsById(signUpDto.getId())).isFalse();

    }

    @Test
    void existById_중복되는_아이디_없어서_성공() throws Exception {

        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();

        // when
        // then
        mockMvc.perform(get(getExistsUri(signUpDto)))
                .andExpect(status().isOk())
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
                .andExpect(status().isBadRequest())
                .andDo(print());


    }

    private String getExistsUri(SignUpDto signUpDto) {
        return uri + "/" + signUpDto.getId() + "/exists";
    }

    @Test
    void login_성공() throws Exception {
        // given
        LoginDto loginDto = TestDto.getLoginDto();
        commonService.signUp(mapper, TestDto.getSignUpDto());

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
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_비밀번호가_틀려서_실패() throws Exception {
        // given
        LoginDto loginDto = TestDto.getLoginDto();
        commonService.signUp(mapper, TestDto.getSignUpDto());
        loginDto.setPassword("WrongPassword");

        // when
        // then
        mockMvc.perform(post(uri + "/login")
                        .content(objectMapper.writeValueAsString(loginDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
