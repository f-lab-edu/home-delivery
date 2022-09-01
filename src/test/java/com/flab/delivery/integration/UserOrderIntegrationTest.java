package com.flab.delivery.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.delivery.annotation.IntegrationTest;
import com.flab.delivery.dto.menu.MenuDto;
import com.flab.delivery.dto.option.OptionDto;
import com.flab.delivery.dto.order.OrderDto;
import com.flab.delivery.dto.order.user.OrderMenuDto;
import com.flab.delivery.dto.order.user.OrderRequestDto;
import com.flab.delivery.enums.PayType;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.fixture.MessageConstants;
import com.flab.delivery.fixture.TestDto;
import com.flab.delivery.mapper.MenuMapper;
import com.flab.delivery.mapper.OptionMapper;
import com.flab.delivery.mapper.OrderMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@IntegrationTest
public class UserOrderIntegrationTest  {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private MockHttpSession session = new MockHttpSession();

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private OptionMapper optionMapper;

    @Autowired
    private OrderMapper orderMapper;


    @BeforeEach
    void init() {
        session.setAttribute(SESSION_ID, "user1");
        session.setAttribute(AUTH_TYPE, UserType.USER);
        orderMapper.save("user1", TestDto.getOrderDto());
    }

    @Test
    void createOrder_권한_없어서_실패() throws Exception {
        MenuDto menu = menuMapper.findByName("후라이드 치킨").get();

        doOrderAuthTest(post("/orders")
                .content(objectMapper.writeValueAsString(getOrderRequestDto(createMenuDto(menu, new ArrayList<>()))))
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

        OrderMenuDto orderMenuDto = createMenuDto(menu, optionList);

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

        OrderMenuDto orderMenuDto = createMenuDto(menu, optionList);


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

        OrderMenuDto orderMenuDto = createMenuDto(menu, optionList);

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
    void createOrder_성공() throws Exception {
        // given
        MenuDto menu = menuMapper.findByName("후라이드 치킨").get();
        List<OptionDto> optionList = optionMapper.findAllByMenuId(menu.getId());

        OrderRequestDto requestDto = getOrderRequestDto(createMenuDto(menu, optionList));


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

    @Test
    void createOrder_같은메뉴_다른옵션_성공() throws Exception {
        // given
        MenuDto menu = menuMapper.findByName("후라이드 치킨").get();
        List<OptionDto> option = optionMapper.findAllByMenuId(menu.getId());

        OrderRequestDto requestDto = getOrderRequestDto(createMenuDto(menu, option), createMenuDto(menu, Arrays.asList(option.get(0))));

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


    @Test
    void getUserOrderList_권한_없어서_실패() throws Exception {
        doOrderAuthTest(get("/orders/user").param("startId", "0"));
    }

    @Test
    void getUserOrderList_성공() throws Exception {
        // given
        for (int i = 0; i < 20; i++) {
            OrderDto orderDto = TestDto.getOrderDto();
            orderMapper.save("user1", orderDto);
        }
        // when
        // then
        mockMvc.perform(get("/orders/user")
                        .session(session))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(MessageConstants.SUCCESS_MESSAGE))
                .andExpect(jsonPath("$.data[*].storeId").exists())
                .andExpect(jsonPath("$.data[*].menuCount").exists())
                .andExpect(jsonPath("$.data[*].menuName").exists())
                .andExpect(jsonPath("$.data.size()").value(10))
                .andExpect(jsonPath("$.data[*].status").exists())
                .andExpect(jsonPath("$.data[*].orderPrice").exists())
                .andExpect(jsonPath("$.data[*].createdAt").exists())
                .andDo(print());
    }

    @Test
    void getUserOrderList_다음_페이지_성공() throws Exception {
        // given
        Long startId = null;

        for (int i = 0; i < 20; i++) {
            OrderDto orderDto = TestDto.getOrderDto();
            orderMapper.save("user1", orderDto);
            if (i == 10) {
                startId = orderDto.getId();
            }
        }

        // when
        // then
        mockMvc.perform(get("/orders/user")
                        .param("startId", String.valueOf(startId))
                        .session(session))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(MessageConstants.SUCCESS_MESSAGE))
                .andExpect(jsonPath("$.data[*].storeId").exists())
                .andExpect(jsonPath("$.data[*].menuCount").exists())
                .andExpect(jsonPath("$.data[*].menuName").exists())
                .andExpect(jsonPath("$.data[*].status").exists())
                .andExpect(jsonPath("$.data.size()").value(10))
                .andExpect(jsonPath("$.data[*].orderPrice").exists())
                .andExpect(jsonPath("$.data[*].createdAt").exists())
                .andDo(print());
    }

    private OrderMenuDto createMenuDto(MenuDto menu, List<OptionDto> optionList) {
        return OrderMenuDto.builder()
                .menuDto(menu)
                .quantity(1)
                .optionList(optionList)
                .build();
    }


    private OrderRequestDto getOrderRequestDto(OrderMenuDto... orderMenuDtos) {

        OrderRequestDto requestDto = OrderRequestDto.builder()
                .storeId(2L)
                .menuList(Arrays.asList(orderMenuDtos))
                .payType(PayType.CARD)
                .build();
        return requestDto;
    }


    private void doOrderAuthTest(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        doAuthTest(mockMvc, requestBuilder);
    }

}
