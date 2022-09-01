package com.flab.delivery.integration;

import com.flab.delivery.AbstractDockerContainer;
import com.flab.delivery.annotation.IntegrationTest;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.fixture.TestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static com.flab.delivery.fixture.CommonTest.doAuthTest;
import static com.flab.delivery.fixture.MessageConstants.SUCCESS_MESSAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@IntegrationTest
public class CategoryIntegrationTest extends AbstractDockerContainer {

    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession mockHttpSession;

    @BeforeEach
    private void init() {
        mockHttpSession = TestDto.createSessionBy("user1", UserType.USER);
    }

    @Test
    void getCategories_권한_없어서_실패() throws Exception {
        doCategoryAuthTest(get("/categories"));
    }


    @Test
    void getCategories_성공() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/categories")
                        .session(mockHttpSession))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("$.data[*].id").exists())
                .andExpect(jsonPath("$.data[*].name").exists());
    }

    @Test
    void getStoreListBy_권한_없어서_실패() throws Exception {
        doCategoryAuthTest(get("/categories/1")
                .param("addressId", "1"));
    }

    @Test
    void getStoreListBy_성공() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/categories/1")
                        .param("addressId", "2")
                        .session(mockHttpSession))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("$.data[*].id").exists())
                .andExpect(jsonPath("$.data[*].detailAddress").exists())
                .andExpect(jsonPath("$.data[*].name").exists())
                .andExpect(jsonPath("$.data[*].status").exists())
                .andExpect(jsonPath("$.data[*].minPrice").exists());
    }

    private void doCategoryAuthTest(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        doAuthTest(mockMvc, requestBuilder);
    }
}
