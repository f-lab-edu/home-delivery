package com.flab.delivery.integration;

import com.flab.delivery.annotation.EnableMockMvc;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.fixture.TestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.flab.delivery.fixture.MessageConstants.HAVE_NO_AUTHORITY_MESSAGE;
import static com.flab.delivery.fixture.MessageConstants.SUCCESS_MESSAGE;
import static com.flab.delivery.utils.SessionConstants.AUTH_TYPE;
import static com.flab.delivery.utils.SessionConstants.SESSION_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@Transactional
@EnableMockMvc
@ActiveProfiles("test")
public class CategoryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession mockHttpSession;

    @BeforeEach
    private void init() {
        mockHttpSession = TestDto.createSessionBy("user1", UserType.USER);
    }

    @Test
    void getCategories_권한_없어서_실패() throws Exception {
        // given
        mockHttpSession.setAttribute(AUTH_TYPE, UserType.ALL);

        // when
        // then
        mockMvc.perform(get("/categories")
                .session(mockHttpSession))
                .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                .andExpect(jsonPath("$.message").value(HAVE_NO_AUTHORITY_MESSAGE));

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
                .andExpect(jsonPath("$.data[*].name").exists())
                .andDo(print());
    }

    @Test
    void getStoreListBy_권한_없어서_실패() throws Exception {
        // given
        mockHttpSession.setAttribute(AUTH_TYPE, UserType.ALL);

        // when
        // then
        mockMvc.perform(get("/categories/1")
                        .param("addressId", "1")
                        .session(mockHttpSession))
                .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                .andExpect(jsonPath("$.message").value(HAVE_NO_AUTHORITY_MESSAGE));
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
                .andExpect(jsonPath("$.data[*].minPrice").exists())
                .andDo(print());
    }
}
