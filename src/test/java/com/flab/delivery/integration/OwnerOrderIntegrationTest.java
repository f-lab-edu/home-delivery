package com.flab.delivery.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.delivery.annotation.IntegrationTest;
import com.flab.delivery.dto.menu.MenuDto;
import com.flab.delivery.dto.option.OptionDto;
import com.flab.delivery.dto.order.OrderDto;
import com.flab.delivery.dto.order.user.OrderMenuDto;
import com.flab.delivery.dto.order.user.OrderMenuHistoryDto;
import com.flab.delivery.dto.order.user.OrderRequestDto;
import com.flab.delivery.dto.pay.PayDto;
import com.flab.delivery.enums.PayType;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.exception.message.ErrorMessageConstants;
import com.flab.delivery.fixture.MessageConstants;
import com.flab.delivery.fixture.TestDto;
import com.flab.delivery.mapper.MenuMapper;
import com.flab.delivery.mapper.OptionMapper;
import com.flab.delivery.mapper.OrderMapper;
import com.flab.delivery.mapper.PayMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Arrays;
import java.util.List;

import static com.flab.delivery.exception.message.ErrorMessageConstants.BAD_INPUT_MESSAGE;
import static com.flab.delivery.fixture.CommonTest.doAuthTest;
import static com.flab.delivery.fixture.MessageConstants.SUCCESS_MESSAGE;
import static com.flab.delivery.utils.SessionConstants.AUTH_TYPE;
import static com.flab.delivery.utils.SessionConstants.SESSION_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@IntegrationTest
public class OwnerOrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PayMapper payMapper;

    private MockHttpSession session = new MockHttpSession();


    @Autowired
    private OrderMapper orderMapper;


    @BeforeEach
    void init() {
        session.setAttribute(SESSION_ID, "user2");
        session.setAttribute(AUTH_TYPE, UserType.OWNER);
    }


    @Test
    void getOwnerOrderList_권한_없어서_실패() throws Exception {
        // given
        // when
        // then
        doOrderAuthTest(get("/orders/owner/1"));
    }
    @Test
    void getOwnerOrderList_주문내역_없어서_빈값_반환() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/orders/owner/1")
                        .session(session))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("$.data[*]").isEmpty())
                .andDo(print());
    }
    @Test
    void getOwnerOrderList_주문내역_있어서_데이터_반환() throws Exception {
        // given
        orderMapper.save("user2", TestDto.getOrderDto());

        // when
        // then
        mockMvc.perform(get("/orders/owner/1")
                        .session(session))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("$.data[*].storeName").exists())
                .andExpect(jsonPath("$.data[*].userPhoneNumber").exists())
                .andExpect(jsonPath("$.data[*].deliveryAddress").exists())
                .andExpect(jsonPath("$.data[*].status").exists())
                .andExpect(jsonPath("$.data[*].createdAt").exists())
                .andExpect(jsonPath("$.data[*].history.menuCount").exists())
                .andExpect(jsonPath("$.data[*].history.menuList[*].menuName").exists())
                .andExpect(jsonPath("$.data[*].history.menuList[*].price").exists())
                .andExpect(jsonPath("$.data[*].history.menuList[*].quantity").exists())
                .andExpect(jsonPath("$.data[*].history.menuList[*].optionList").exists())
                .andDo(print());
    }

    @Test
    void approveOrder_권한_없어서_실패() throws Exception {
        // given
        OrderDto orderDto = TestDto.getOrderDto();
        orderMapper.save("user2", orderDto);

        // when
        // then
        doOrderAuthTest(patch("/orders/"+orderDto.getId()+"/owner/approve"));
    }

    @Test
    void approveOrder_잘못된_입력으로_실패() throws Exception {
        // given
        OrderDto orderDto = TestDto.getOrderDto();
        orderMapper.save("user2", orderDto);

        // when
        // then
        mockMvc.perform(patch("/orders/99999/owner/approve")
                        .session(session))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(BAD_INPUT_MESSAGE));
    }

    @Test
    void approveOrder_성공() throws Exception {
        // given
        OrderDto orderDto = TestDto.getOrderDto();
        orderMapper.save("user2", orderDto);
        PayDto payDto = PayDto.completePay(orderDto.getId(), PayType.CARD);
        payMapper.save(payDto);

        // when
        // then
        mockMvc.perform(patch("/orders/"+orderDto.getId()+"/owner/approve")
                        .session(session))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(SUCCESS_MESSAGE));
    }



    @Test
    void cancelOrder_권한_없어서_실패() throws Exception {
        // given
        OrderDto orderDto = TestDto.getOrderDto();
        orderMapper.save("user2", orderDto);

        // when
        // then
        doOrderAuthTest(patch("/orders/"+orderDto.getId()+"/owner/cancel"));
    }

    @Test
    void cancelOrder_잘못된_입력으로_실패() throws Exception {
        // given
        OrderDto orderDto = TestDto.getOrderDto();
        orderMapper.save("user2", orderDto);

        // when
        // then
        mockMvc.perform(patch("/orders/99999/owner/cancel")
                        .session(session))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(BAD_INPUT_MESSAGE));
    }

    @Test
    void cancelOrder_성공() throws Exception {
        // given
        OrderDto orderDto = TestDto.getOrderDto();
        orderMapper.save("user2", orderDto);
        PayDto payDto = PayDto.completePay(orderDto.getId(), PayType.CARD);
        payMapper.save(payDto);

        // when
        // then
        mockMvc.perform(patch("/orders/"+orderDto.getId()+"/owner/cancel")
                        .session(session))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(SUCCESS_MESSAGE));
    }


    private void doOrderAuthTest(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        doAuthTest(mockMvc, requestBuilder);
    }

}
