package com.flab.delivery.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.delivery.annotation.IntegrationTest;
import com.flab.delivery.dto.option.OptionDto;
import com.flab.delivery.dto.option.OptionRequestDto;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.fixture.MessageConstants;
import com.flab.delivery.mapper.OptionMapper;
import com.flab.delivery.utils.SessionConstants;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@IntegrationTest
class OptionIntegrationTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    MockHttpSession mockHttpSession = new MockHttpSession();

    @Nested
    @DisplayName("POST : /options")
    class CreateOption {
        private String ownerId = "user2";
        private UserType userType = UserType.OWNER;
        private String url = "/options";

        private Long menuId = 1L;
        private String name = "치츠볼 추가";
        private Integer price = 5000;

        private OptionRequestDto getRequestDto() {
            return OptionRequestDto.builder()
                    .menuId(menuId)
                    .name(name)
                    .price(price)
                    .build();
        }

        @BeforeEach
        void setUp() {
            mockHttpSession.setAttribute(SessionConstants.SESSION_ID, ownerId);
            mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, userType);
        }

        @Nested
        @DisplayName("성공")
        class Success {
            @Test
            @DisplayName("생성 성공")
            void success() throws Exception {
                String json = objectMapper.writeValueAsString(getRequestDto());
                mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("실패")
        class Fail {

            @Nested
            @DisplayName("메뉴아이디")
            class MenuId {

                @Test
                @DisplayName("Null인 경우")
                void menuIdNull() throws Exception {
                    menuId = null;
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("메뉴아이디 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("음수인 경우")
                void negative() throws Exception {
                    menuId = -1L;
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("메뉴아이디 정보가 올바르지 않습니다"))
                            .andDo(print());
                }
            }

            @Nested
            @DisplayName("옵션 이름")
            class Name {
                @Test
                @DisplayName("Null인 경우")
                void nameNull() throws Exception {
                    name = null;
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("옵션이름 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("공백인 경우")
                void blank() throws Exception {
                    name = " ";
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("옵션이름 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

            }

            @Nested
            @DisplayName("옵션 가격")
            class Price {

                @Test
                @DisplayName("음수인 경우")
                void negative() throws Exception {
                    price = -1;
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("옵션가격 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("Null인 경우")
                void priceNull() throws Exception {
                    price = null;
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("옵션가격 정보가 올바르지 않습니다"))
                            .andDo(print());
                }


            }

            @Nested
            @DisplayName("권한")
            class UserType {
                @Test
                @DisplayName("권한 - 유저인경우")
                void userTypeUser() throws Exception {
                    mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, com.flab.delivery.enums.UserType.USER);
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                            .andExpect(jsonPath("$.message").value(MessageConstants.HAVE_NO_AUTHORITY_MESSAGE))
                            .andDo(print());
                }

                @Test
                @DisplayName("권한 - 유저인경우")
                void userTypeRider() throws Exception {
                    mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, com.flab.delivery.enums.UserType.RIDER);
                    String json = objectMapper.writeValueAsString(getRequestDto());

                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                            .andExpect(jsonPath("$.message").value(MessageConstants.HAVE_NO_AUTHORITY_MESSAGE))
                            .andDo(print());

                }
            }

        }

    }

    @Nested
    @DisplayName("PATCH : /options/{id}")
    class UpdateOption {
        // 5000원에서 -> 10000원으로
        private String ownerId = "user2";
        private UserType userType = UserType.OWNER;
        private String url = "/options/3";


        private Long id = 3L;
        private Long menuId = 1L;
        private String name = "치츠볼 추가";
        private Integer price = 10000;

        private OptionRequestDto getRequestDto() {
            return OptionRequestDto.builder()
                    .menuId(menuId)
                    .name(name)
                    .price(price)
                    .build();
        }

        @BeforeEach
        void setUp() {
            mockHttpSession.setAttribute(SessionConstants.SESSION_ID, ownerId);
            mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, userType);
        }

        @Nested
        @DisplayName("성공")
        class Success {

            @Test
            @DisplayName("변경 성공 - Price = 5000원 -> 10000원")
            void success(@Autowired OptionMapper optionMapper) throws Exception {
                // given
                OptionDto before = optionMapper.findById(id).get();
                String json = objectMapper.writeValueAsString(getRequestDto());
                // when
                mockMvc.perform(patch(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                        .andDo(print());

                OptionDto after = optionMapper.findById(id).get();
                // then
                Assertions.assertNotEquals(before.getPrice(), after.getPrice());
            }
        }

        @Nested
        @DisplayName("실패")
        class Fail {
            @Nested
            @DisplayName("메뉴아이디")
            class MenuId {

                @Test
                @DisplayName("Null인 경우")
                void menuIdNull() throws Exception {
                    menuId = null;
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(patch(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("메뉴아이디 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("음수인 경우")
                void negative() throws Exception {
                    menuId = -1L;
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(patch(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("메뉴아이디 정보가 올바르지 않습니다"))
                            .andDo(print());
                }
            }

            @Nested
            @DisplayName("옵션 이름")
            class Name {
                @Test
                @DisplayName("Null인 경우")
                void nameNull() throws Exception {
                    name = null;
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(patch(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("옵션이름 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("공백인 경우")
                void blank() throws Exception {
                    name = " ";
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(patch(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("옵션이름 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

            }

            @Nested
            @DisplayName("옵션 가격")
            class Price {

                @Test
                @DisplayName("음수인 경우")
                void negative() throws Exception {
                    price = -1;
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(patch(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("옵션가격 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("Null인 경우")
                void priceNull() throws Exception {
                    price = null;
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(patch(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("옵션가격 정보가 올바르지 않습니다"))
                            .andDo(print());
                }


            }


            @Nested
            @DisplayName("권한")
            class UserType {
                @Test
                @DisplayName("권한 - 유저인경우")
                void userTypeUser() throws Exception {
                    mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, com.flab.delivery.enums.UserType.USER);
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(patch(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                            .andExpect(jsonPath("$.message").value(MessageConstants.HAVE_NO_AUTHORITY_MESSAGE))
                            .andDo(print());
                }

                @Test
                @DisplayName("권한 - 유저인경우")
                void userTypeRider() throws Exception {
                    mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, com.flab.delivery.enums.UserType.RIDER);
                    String json = objectMapper.writeValueAsString(getRequestDto());

                    mockMvc.perform(patch(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                            .andExpect(jsonPath("$.message").value(MessageConstants.HAVE_NO_AUTHORITY_MESSAGE))
                            .andDo(print());
                }
            }
        }
    }

    @Nested
    @DisplayName("DELTE : /options/{id}")
    class DeleteOption {
        private String ownerId = "user2";
        private UserType userType = UserType.OWNER;
        private String url = "/options/3";

        private Long id = 3L;

        @BeforeEach
        void setUp() {
            mockHttpSession.setAttribute(SessionConstants.SESSION_ID, ownerId);
            mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, userType);
        }


        @Nested
        @DisplayName("성공")
        class Success {
            @Test
            @DisplayName("삭제 성공")
            void success(@Autowired OptionMapper optionMapper) throws Exception {
                mockMvc.perform(delete(url).session(mockHttpSession))
                        .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                        .andDo(print());
                Assertions.assertFalse(optionMapper.findById(id).isPresent());
            }
        }

        @Nested
        @DisplayName("실패")
        class Fail {
            @Nested
            @DisplayName("권한")
            class UserType {
                @Test
                @DisplayName("권한 - 유저인경우")
                void userTypeUser() throws Exception {
                    mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, com.flab.delivery.enums.UserType.USER);

                    mockMvc.perform(delete(url).session(mockHttpSession))
                            .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                            .andExpect(jsonPath("$.message").value(MessageConstants.HAVE_NO_AUTHORITY_MESSAGE))
                            .andDo(print());
                }

                @Test
                @DisplayName("권한 - 유저인경우")
                void userTypeRider() throws Exception {
                    mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, com.flab.delivery.enums.UserType.RIDER);

                    mockMvc.perform(delete(url).session(mockHttpSession))
                            .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                            .andExpect(jsonPath("$.message").value(MessageConstants.HAVE_NO_AUTHORITY_MESSAGE))
                            .andDo(print());
                }
            }
        }

    }

    @Nested
    @DisplayName("GET : /options?menuId={menuId}")
    class GetOptionList {
        private String ownerId = "user2";
        private UserType userType = UserType.OWNER;
        private String url = "/options";

        private final String paramName = "menuId";
        private final String paramValue = "1";

        @BeforeEach
        void setUp() {
            mockHttpSession.setAttribute(SessionConstants.SESSION_ID, ownerId);
            mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, userType);
        }


        @Test
        @DisplayName("조회 성공")
        void success() throws Exception {
            mockMvc.perform(get(url).param(paramName, paramValue).session(mockHttpSession))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.data.[0].name").value("치즈볼 추가"))
                    .andDo(print());

        }

    }
}