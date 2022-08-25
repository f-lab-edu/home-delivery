package com.flab.delivery.integration;

import com.flab.delivery.AbstractRedisContainer;
import com.flab.delivery.annotation.IntegrationTest;
import com.flab.delivery.dao.RiderDao;
import com.flab.delivery.dto.order.OrderDto;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.fixture.TestDto;
import com.flab.delivery.mapper.OrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static com.flab.delivery.fixture.CommonTest.doAuthTest;
import static com.flab.delivery.fixture.MessageConstants.SUCCESS_MESSAGE;
import static com.flab.delivery.utils.SessionConstants.AUTH_TYPE;
import static com.flab.delivery.utils.SessionConstants.SESSION_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@IntegrationTest
public class RiderOrderIntegrationTest extends AbstractRedisContainer {


    public static final String RIDER_ID = "rider1";
    public static final Long ADDRESS_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RiderDao riderDao;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private MockHttpSession session = new MockHttpSession();

    @BeforeEach
    void init() {
        session.setAttribute(SESSION_ID, RIDER_ID);
        session.setAttribute(AUTH_TYPE, UserType.RIDER);
        redisTemplate.opsForZSet().removeRange("ORDER" + ADDRESS_ID, 0, -1);
    }

    @Test
    void getDeliveryRequestList_권한_없어서_실패() throws Exception {
        // given
        // when
        // then
        doOrderAuthTest(get("/orders/rider/request")
                .param("addressId", "1"));
    }

    @Test
    void getDeliveryRequestList_성공() throws Exception {
        // given
        riderDao.registerStandByRider(RIDER_ID, ADDRESS_ID);
        for (int i = 0; i < 5; i++) {
            riderDao.addOrderBy(ADDRESS_ID, TestDto.getOrderDeliveryDto((long) i));
        }

        // when
        // then
        mockMvc.perform(get("/orders/rider/request")
                        .param("addressId", "1")
                        .session(session))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("$.data[*].orderId").exists())
                .andExpect(jsonPath("$.data[*].addressId").exists())
                .andExpect(jsonPath("$.data[*].storeName").exists())
                .andExpect(jsonPath("$.data[*].storeAddress").exists())
                .andExpect(jsonPath("$.data[*].deliveryAddress").exists())
                .andExpect(jsonPath("$.data[*].userPhoneNumber").exists())
                .andDo(print());

    }

    @Test
    void acceptDelivery_권한_없어서_실패() throws Exception {
        // given
        // when
        // then
        doOrderAuthTest(patch("/orders/1/rider/accept")
                .param("addressId", "1"));
    }

    @Test
    void acceptDelivery_성공() throws Exception {
        // given
        riderDao.registerStandByRider(RIDER_ID, ADDRESS_ID);
        for (int i = 0; i < 5; i++) {
            riderDao.addOrderBy(ADDRESS_ID, TestDto.getOrderDeliveryDto((long) i));
        }

        // when
        // then
        mockMvc.perform(patch("/orders/1/rider/accept")
                        .param("addressId", "1")
                        .session(session))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(SUCCESS_MESSAGE));
    }

    @Test
    void finishDelivery_권한_없어서_실패() throws Exception {
        // given
        // when
        // then
        doOrderAuthTest(patch("/orders/1/rider/finish")
                .param("addressId", "1"));
    }

    @Test
    void finishDelivery_성공() throws Exception {
        // given
        riderDao.registerStandByRider(RIDER_ID, ADDRESS_ID);

        // when
        // then
        mockMvc.perform(patch("/orders/1/rider/finish")
                        .param("addressId", "1")
                        .session(session))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(SUCCESS_MESSAGE));
    }


    @Test
    void getInDeliveryList_권한_없어서_실패() throws Exception {
        // given
        // when
        // then
        doOrderAuthTest(get("/orders/rider/in-delivery")
                .param("addressId", "1"));
    }

    @Test
    void getInDeliveryList_성공() throws Exception {
        // given
        riderDao.registerStandByRider(RIDER_ID, ADDRESS_ID);

        for (int i = 0; i < 15; i++) {
            OrderDto dto = TestDto.getOrderDto();
            orderMapper.save("user1", dto);
            orderMapper.updateOrderForDelivery(dto.getId(), RIDER_ID);
        }

        // when
        // then
        mockMvc.perform(get("/orders/rider/in-delivery")
                        .param("addressId", "1")
                        .session(session))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("$.data[*].orderId").exists())
                .andExpect(jsonPath("$.data[*].addressId").exists())
                .andExpect(jsonPath("$.data[*].storeName").exists())
                .andExpect(jsonPath("$.data[*].storeAddress").exists())
                .andExpect(jsonPath("$.data[*].deliveryAddress").exists())
                .andExpect(jsonPath("$.data[*].status").exists())
                .andExpect(jsonPath("$.data[*].startDeliveryTime").exists())
                .andExpect(jsonPath("$.data[*].userPhoneNumber").exists())
                .andDo(print());

    }


    @Test
    void getFinishDeliveryList_권한_없어서_실패() throws Exception {
        // given
        // when
        // then
        doOrderAuthTest(get("/orders/rider/finish")
                .param("addressId", "1"));
    }

    @Test
    void getFinishDeliveryList_성공() throws Exception {
        // given
        riderDao.registerStandByRider(RIDER_ID, ADDRESS_ID);

        for (int i = 0; i < 15; i++) {
            OrderDto dto = TestDto.getOrderDto();
            orderMapper.save("user1", dto);
            orderMapper.updateOrderForDelivery(dto.getId(), RIDER_ID);
            orderMapper.updateOrderForFinish(dto.getId(), RIDER_ID);
        }

        // when
        // then
        mockMvc.perform(get("/orders/rider/finish")
                        .param("addressId", "1")
                        .session(session))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("$.data[*].orderId").exists())
                .andExpect(jsonPath("$.data[*].addressId").exists())
                .andExpect(jsonPath("$.data[*].storeName").exists())
                .andExpect(jsonPath("$.data[*].storeAddress").exists())
                .andExpect(jsonPath("$.data[*].deliveryAddress").exists())
                .andExpect(jsonPath("$.data[*].status").exists())
                .andExpect(jsonPath("$.data[*].startDeliveryTime").exists())
                .andExpect(jsonPath("$.data[*].endDeliveryTime").exists())
                .andExpect(jsonPath("$.data[*].userPhoneNumber").exists())
                .andDo(print());

    }

    private void doOrderAuthTest(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        doAuthTest(mockMvc, requestBuilder);
    }
}
