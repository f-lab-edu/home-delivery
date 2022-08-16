package com.flab.delivery.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.delivery.annotation.IntegrationTest;
import com.flab.delivery.dto.menu.MenuDto;
import com.flab.delivery.dto.option.OptionDto;
import com.flab.delivery.dto.order.OrderMenuDto;
import com.flab.delivery.dto.order.OrderRequestDto;
import com.flab.delivery.enums.PayType;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.fixture.MessageConstants;
import com.flab.delivery.fixture.TestDto;
import com.flab.delivery.mapper.MenuMapper;
import com.flab.delivery.mapper.OptionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.flab.delivery.fixture.CommonTest.doAuthTest;
import static com.flab.delivery.utils.SessionConstants.AUTH_TYPE;
import static com.flab.delivery.utils.SessionConstants.SESSION_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@IntegrationTest
public class OrderIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    MockHttpSession session = new MockHttpSession();

    @Autowired
    MenuMapper menuMapper;

    @Autowired
    OptionMapper optionMapper;


    @BeforeEach
    void init() {
        session.setAttribute(SESSION_ID, "user1");
        session.setAttribute(AUTH_TYPE, UserType.USER);
    }

    @Test
    void createOrder_권한_없어서_실패() throws Exception {
        doOrderAuthTest(post("/orders")
                .content(objectMapper.writeValueAsString(getOrderRequestDto()))
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void createOrder_메뉴목록_검증으로_인한_실패() throws Exception {
        // given
        OrderRequestDto wrongDto = OrderRequestDto.builder()
                .storeId(1L)
                .menuList(new ArrayList<>())
                .payType(PayType.CARD)
                .build();

        // when
        // then
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongDto))
                        .session(session))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("주문 하실 메뉴가 없습니다."))
                .andDo(print());
    }

    @Test
    void createOrder_결제타입_검증으로_인한_실패() throws Exception {
        // given

        MenuDto menu = menuMapper.findByName("후라이드 치킨").get();
        List<OptionDto> optionList = optionMapper.findAllByMenuId(menu.getId());

        OrderMenuDto orderMenuDto = OrderMenuDto.builder()
                .menuDto(menu)
                .quantity(1)
                .optionList(optionList)
                .build();

        OrderRequestDto wrongDto = OrderRequestDto.builder()
                .storeId(2L)
                .menuList(Arrays.asList(orderMenuDto))
                .build();

        // when
        // then
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongDto))
                        .session(session))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("결제 방법이 옳바르지 않습니다"))
                .andDo(print());
    }

    @Test
    void createOrder_매장_오픈이_아니라서_실패() throws Exception {
        // given

        MenuDto menu = menuMapper.findByName("후라이드 치킨").get();
        List<OptionDto> optionList = optionMapper.findAllByMenuId(menu.getId());

        OrderMenuDto orderMenuDto = OrderMenuDto.builder()
                .menuDto(menu)
                .quantity(1)
                .optionList(optionList)
                .build();


        OrderRequestDto wrongDto = OrderRequestDto.builder()
                .storeId(1L)
                .menuList(Arrays.asList(orderMenuDto))
                .payType(PayType.CARD)
                .build();

        // when
        // then
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongDto))
                        .session(session))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("매장이 오픈 상태가 아닙니다."))
                .andDo(print());
    }

    @Test
    void createOrder_입력된_가격이_잘못돼서_실패() throws Exception {

        // given
        MenuDto menu = menuMapper.findByName("후라이드 치킨").get();
        List<OptionDto> optionList = optionMapper.findAllByMenuId(menu.getId());

        OrderMenuDto orderMenuDto = OrderMenuDto.builder()
                .menuDto(menu)
                .quantity(1)
                .optionList(optionList)
                .build();

        OrderRequestDto wrongDto = OrderRequestDto.builder()
                .storeId(1L)
                .menuList(Arrays.asList(orderMenuDto))
                .payType(PayType.CARD)
                .build();

        // when
        // then
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongDto))
                        .session(session))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("매장이 오픈 상태가 아닙니다."))
                .andDo(print());
    }

    //TODO


    @Test
    void createOrder_성공() throws Exception {
        // given

        OrderRequestDto requestDto = getOrderRequestDto();

        // when
        // then
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .session(session))
                .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value(MessageConstants.SUCCESS_MESSAGE))
                .andDo(print());
    }

    private OrderRequestDto getOrderRequestDto() {
        MenuDto menu = menuMapper.findByName("후라이드 치킨").get();
        List<OptionDto> optionList = optionMapper.findAllByMenuId(menu.getId());

        OrderMenuDto orderMenuDto = OrderMenuDto.builder()
                        .menuDto(menu)
                        .quantity(1)
                        .optionList(optionList)
                        .build();


        OrderRequestDto requestDto = OrderRequestDto.builder()
                .storeId(2L)
                .menuList(Arrays.asList(orderMenuDto))
                .payType(PayType.CARD)
                .build();
        return requestDto;
    }


    private void doOrderAuthTest(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        doAuthTest(mockMvc, requestBuilder);
    }

}
