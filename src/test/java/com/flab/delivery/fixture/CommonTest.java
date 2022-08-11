package com.flab.delivery.fixture;

import com.flab.delivery.enums.UserType;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static com.flab.delivery.fixture.MessageConstants.HAVE_NO_AUTHORITY_MESSAGE;
import static com.flab.delivery.utils.SessionConstants.AUTH_TYPE;
import static com.flab.delivery.utils.SessionConstants.SESSION_ID;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public final class CommonTest {

    private static final MockHttpSession MOCK_HTTP_SESSION = new MockHttpSession();

    public static void doAuthTest(MockMvc mockMvc, MockHttpServletRequestBuilder requestBuilder) throws Exception {
        // given
        MOCK_HTTP_SESSION.setAttribute(SESSION_ID, "user1");
        MOCK_HTTP_SESSION.setAttribute(AUTH_TYPE, UserType.ALL);

        // when
        // then
        mockMvc.perform(requestBuilder.session(MOCK_HTTP_SESSION))
                .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                .andExpect(jsonPath("$.message").value(HAVE_NO_AUTHORITY_MESSAGE));
    }
}
