package com.flab.delivery.integration;

import com.flab.delivery.annotation.IntegrationTest;
import com.flab.delivery.dao.RiderDao;
import com.flab.delivery.enums.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static com.flab.delivery.fixture.CommonTest.doAuthTest;
import static com.flab.delivery.fixture.MessageConstants.SUCCESS_MESSAGE;
import static com.flab.delivery.utils.SessionConstants.AUTH_TYPE;
import static com.flab.delivery.utils.SessionConstants.SESSION_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@IntegrationTest
public class RiderIntegrationTestDocker  {

    private static final String RIDER_ID = "rider1";
    @Autowired
    MockMvc mockMvc;

    @Autowired
    RiderDao riderDao;

    MockHttpSession mockHttpSession = new MockHttpSession();

    @BeforeEach
    void init() {
        mockHttpSession.setAttribute(SESSION_ID, RIDER_ID);
        mockHttpSession.setAttribute(AUTH_TYPE, UserType.RIDER);
    }

    @Test
    void registerStandByRider_권한_없어서_실패() throws Exception {
        doRiderAuthTest(post("/riders/standby")
                .param("addressId", "1"));
    }

    @Test
    void registerStandByRider_성공() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(post("/riders/standby")
                        .param("addressId", "1")
                        .session(mockHttpSession))
                .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value(SUCCESS_MESSAGE));
    }

    @Test
    void deleteStandByRider_권한_없어서_실패() throws Exception {
        doRiderAuthTest(delete("/riders/standby")
                .param("addressId", "1"));
    }

    @Test
    void deleteStandByRider_성공() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(delete("/riders/standby")
                        .param("addressId", "1")
                        .session(mockHttpSession))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(SUCCESS_MESSAGE));

    }

    private void doRiderAuthTest(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        doAuthTest(mockMvc, requestBuilder);
    }
}
