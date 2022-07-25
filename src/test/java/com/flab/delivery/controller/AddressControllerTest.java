package com.flab.delivery.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.delivery.annotation.EnableMockMvc;
import com.flab.delivery.dto.address.AddressRequestDto;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.fixture.TestDto;
import com.flab.delivery.service.AddressService;
import com.flab.delivery.utils.SessionConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static com.flab.delivery.fixture.MessageConstants.HAVE_NO_AUTHORITY_MESSAGE;
import static com.flab.delivery.fixture.MessageConstants.SUCCESS_MESSAGE;
import static com.flab.delivery.utils.SessionConstants.AUTH_TYPE;
import static com.flab.delivery.utils.SessionConstants.SESSION_ID;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@Transactional
@EnableMockMvc
@ActiveProfiles("test")
class AddressControllerTest {

    @Autowired
    AddressService addressService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    MockHttpSession mockHttpSession = new MockHttpSession();

    @BeforeEach
    void init() {
        mockHttpSession.setAttribute(SESSION_ID, "user1");
        mockHttpSession.setAttribute(AUTH_TYPE, UserType.USER);
    }

    @Test
    void addAddress_권한_없는_사용자_실패() throws Exception {
        // given
        mockHttpSession.setAttribute(AUTH_TYPE, UserType.ALL);
        AddressRequestDto addressRequestDto = TestDto.getAddressRequestDto();

        // when
        // then
        mockMvc.perform(post("/locations")
                        .content(objectMapper.writeValueAsString(addressRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(mockHttpSession))
                .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                .andExpect(jsonPath("$.message").value(HAVE_NO_AUTHORITY_MESSAGE));

    }


    @Test
    void addAddress_db에_없는_주소_실패() throws Exception {
        // given
        AddressRequestDto addressRequestDto = AddressRequestDto.builder()
                .townName("WorngTown")
                .detailAddress("15번길 13로")
                .build();

        // when
        // then
        mockMvc.perform(post("/locations")
                        .content(objectMapper.writeValueAsString(addressRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(mockHttpSession))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("존재하지 않는 주소 입니다."));

    }

    @Test
    void addAddress_주소_입력_없어서_실패() throws Exception {
        // given
        AddressRequestDto addressRequestDto = AddressRequestDto.builder()
                .detailAddress("15번길 13로")
                .build();

        // when
        // then
        mockMvc.perform(post("/locations")
                        .content(objectMapper.writeValueAsString(addressRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(mockHttpSession))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("주소는 필수 입력값입니다"));

    }

    @Test
    void addAddress_성공() throws Exception {
        // given
        AddressRequestDto addressRequestDto = TestDto.getAddressRequestDto();

        // when
        // then
        mockMvc.perform(post("/locations")
                        .content(objectMapper.writeValueAsString(addressRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(mockHttpSession))
                .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value(SUCCESS_MESSAGE));

    }


    @Test
    void getAllAddress_권한_없는_사용자_실패() throws Exception {
        // given
        mockHttpSession.setAttribute(AUTH_TYPE, UserType.ALL);

        // when
        // then
        mockMvc.perform(get("/locations")
                        .session(mockHttpSession))
                .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                .andExpect(jsonPath("$.message").value(HAVE_NO_AUTHORITY_MESSAGE));
    }

    @Test
    void getAllAddress_성공() throws Exception {

        // given
        // when
        // then
        mockMvc.perform(get("/locations")
                        .session(mockHttpSession))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("$.data[*].townName").exists())
                .andExpect(jsonPath("$.data[*].detailAddress").exists())
                .andExpect(jsonPath("$.data[*].alias").exists())
                .andExpect(jsonPath("$.data[*].selected").exists())
                .andDo(print());

    }

}