package com.flab.delivery.integration;

import com.flab.delivery.AbstractRedisContainer;
import com.flab.delivery.annotation.IntegrationTest;
import com.flab.delivery.dto.order.OrderDto;
import com.flab.delivery.dto.pay.PayDto;
import com.flab.delivery.enums.PayType;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.fixture.TestDto;
import com.flab.delivery.mapper.OrderMapper;
import com.flab.delivery.mapper.PayMapper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static com.flab.delivery.exception.message.ErrorMessageConstants.BAD_REQUEST_MESSAGE;
import static com.flab.delivery.fixture.CommonTest.doAuthTest;
import static com.flab.delivery.fixture.MessageConstants.SUCCESS_MESSAGE;
import static com.flab.delivery.utils.SessionConstants.AUTH_TYPE;
import static com.flab.delivery.utils.SessionConstants.SESSION_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@IntegrationTest
public class OwnerOrderIntegrationTest extends AbstractRedisContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PayMapper payMapper;

    private MockHttpSession session = new MockHttpSession();

    @Autowired
    private OrderMapper orderMapper;
    private OrderDto orderDto;

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
        createOrder();
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
        createOrder();
        // when
        // then
        doOrderAuthTest(patch(getApproveOrderUri(orderDto.getId())));
    }

    @Test
    void approveOrder_잘못된_입력으로_실패() throws Exception {
        // given
        createOrder();
        // when
        // then
        mockMvc.perform(patch(getApproveOrderUri(99999L))
                        .session(session))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(BAD_REQUEST_MESSAGE));
    }

    @Test
    void approveOrder_성공() throws Exception {
        // given
        createOrder();
        PayDto payDto = PayDto.completePay(orderDto.getId(), PayType.CARD);
        payMapper.save(payDto);

        // when
        // then
        mockMvc.perform(patch(getApproveOrderUri(orderDto.getId()))
                        .session(session))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(SUCCESS_MESSAGE));
    }


    @NotNull
    private String getApproveOrderUri(Long orderId) {
        return "/orders/" + orderId + "/owner/approve";
    }

    @Test
    void cancelOrder_권한_없어서_실패() throws Exception {
        // given
        createOrder();
        // when
        // then
        doOrderAuthTest(patch(getCancelOrderUri(orderDto.getId())));
    }

    @Test
    void cancelOrder_잘못된_입력으로_실패() throws Exception {
        // given
        createOrder();
        // when
        // then
        mockMvc.perform(patch(getCancelOrderUri(99999L))
                        .session(session))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(BAD_REQUEST_MESSAGE));
    }

    @Test
    void cancelOrder_성공() throws Exception {
        // given
        createOrder();
        PayDto payDto = PayDto.completePay(orderDto.getId(), PayType.CARD);
        payMapper.save(payDto);

        // when
        // then
        mockMvc.perform(patch(getCancelOrderUri(orderDto.getId()))
                        .session(session))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(SUCCESS_MESSAGE));
    }

    @NotNull
    private String getCancelOrderUri(Long orderId) {
        return "/orders/" + orderId + "/owner/cancel";
    }

    @Test
    void callRider_권한_없어서_실패() throws Exception {
        // given
        createOrder();
        // when
        // then
        doOrderAuthTest(post(getCallRiderURI(orderDto.getId(), orderDto.getStoreId())));
    }

    @Test
    void callRider_성공() throws Exception {
        // given
        createOrder();
        // when
        // then
        mockMvc.perform(post(getCallRiderURI(orderDto.getId(), orderDto.getStoreId()))
                        .session(session))
                .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value(SUCCESS_MESSAGE));

    }

    @NotNull
    private String getCallRiderURI(Long orderId, Long storeId) {
        return "/orders/" + orderId + "/owner/" + storeId + "/call";
    }

    private void createOrder() {
        orderDto = TestDto.getOrderDto();
        orderMapper.save("user2", orderDto);
    }

    private void doOrderAuthTest(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        doAuthTest(mockMvc, requestBuilder);
    }

}
