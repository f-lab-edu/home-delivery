package com.flab.delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.TestDto;
import com.flab.delivery.mapper.CommonMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public abstract class AbstractSignUpTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    CommonMapper mapper;
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
                .andExpect(status().isOk());

        //then
        assertThat(mapper.existById(signUpDto.getId())).isTrue();
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
        assertThat(mapper.existById(signUpDto.getId())).isFalse();

    }


    @Test
    void signUp_이미_존재하는_아이디로_실패() throws Exception {

        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();
        mapper.save(signUpDto);

        // when
        mockMvc.perform(post(uri)
                        .content(objectMapper.writeValueAsString(signUpDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());

        //then
        assertThat(mapper.existById(signUpDto.getId())).isTrue();
        assertThat(mapper.countById()).isEqualTo(1);

    }
}
