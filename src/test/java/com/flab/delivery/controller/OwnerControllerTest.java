package com.flab.delivery.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.TestDto;
import com.flab.delivery.mapper.OwnerMapper;
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
class OwnerControllerTest {

    public static final String OWNERS = "/owners";
    @Autowired
    MockMvc mockMvc;

    @Autowired
    OwnerMapper ownerMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void signUp_성공() throws Exception {
        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();

        // when
        mockMvc.perform(post(OWNERS)
                        .content(objectMapper.writeValueAsString(signUpDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //then
        assertThat(ownerMapper.existById(signUpDto.getId())).isTrue();
    }

    @Test
    void signUp_파라미터_검증에_잡혀_실패() throws Exception {

        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();
        signUpDto.setPassword("asd1");

        // when
        mockMvc.perform(post(OWNERS)
                        .content(objectMapper.writeValueAsString(signUpDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());

        //then
        assertThat(ownerMapper.existById(signUpDto.getId())).isFalse();

    }


    @Test
    void signUp_이미_존재하는_ID로_실패() throws Exception {

        // given
        SignUpDto signUpDto = TestDto.getSignUpDto();
        ownerMapper.save(signUpDto);

        // when
        mockMvc.perform(post(OWNERS)
                        .content(objectMapper.writeValueAsString(signUpDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());

        //then
        assertThat(ownerMapper.existById(signUpDto.getId())).isTrue();
        assertThat(ownerMapper.countById()).isEqualTo(1);

    }

}