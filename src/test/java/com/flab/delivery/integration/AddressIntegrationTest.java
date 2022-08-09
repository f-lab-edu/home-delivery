package com.flab.delivery.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.delivery.annotation.IntegrationTest;
import com.flab.delivery.dto.address.AddressRequestDto;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.fixture.TestDto;
import com.flab.delivery.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static com.flab.delivery.fixture.CommonTest.doAuthTest;
import static com.flab.delivery.fixture.MessageConstants.SUCCESS_MESSAGE;
import static com.flab.delivery.utils.SessionConstants.AUTH_TYPE;
import static com.flab.delivery.utils.SessionConstants.SESSION_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@IntegrationTest
class AddressIntegrationTest {

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
        AddressRequestDto addressRequestDto = TestDto.getAddressRequestDto();

        doAddressAuthTest(post("/locations")
                .content(objectMapper.writeValueAsString(addressRequestDto))
                .contentType(MediaType.APPLICATION_JSON));

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
        doAddressAuthTest(get("/locations"));
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
                .andExpect(jsonPath("$.data[*].id").exists())
                .andExpect(jsonPath("$.data[*].townName").exists())
                .andExpect(jsonPath("$.data[*].detailAddress").exists())
                .andExpect(jsonPath("$.data[*].alias").exists())
                .andExpect(jsonPath("$.data[*].selected").exists())
                .andDo(print());

    }


    @Test
    void deleteAddress_권한_없는_사용자_실패() throws Exception {
        doAddressAuthTest(delete("/locations/1"));
    }

    @Test
    void deleteAddress_없는_주소_실패() throws Exception {

        // given
        // when
        // then
        mockMvc.perform(delete("/locations/0")
                        .session(mockHttpSession))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("존재하지 않는 주소 입니다."));
    }

    @Test
    void deleteAddress_성공() throws Exception {

        // given
        // when
        // then
        mockMvc.perform(delete("/locations/15")
                        .session(mockHttpSession))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(SUCCESS_MESSAGE));
    }


    @Test
    void selectAddress_권한_없는_사용자_실패() throws Exception {
        doAddressAuthTest(patch("/locations/1"));
    }

    @Test
    void selectAddress_잘못된_요청_실패() throws Exception {
        // given
        mockHttpSession.setAttribute(SESSION_ID, "user2");
        // when
        // then
        mockMvc.perform(patch("/locations/15")
                        .session(mockHttpSession))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("잘못된 요청 입니다."));
    }

    @Test
    void selectAddress_성공() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(patch("/locations/15")
                        .session(mockHttpSession))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(SUCCESS_MESSAGE));
    }

    private void doAddressAuthTest(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        doAuthTest(mockMvc, requestBuilder);
    }
}