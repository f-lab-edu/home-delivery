package com.flab.delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.delivery.annotation.EnableMockMvc;
import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.store.StoreDto;
import com.flab.delivery.dto.store.StoreRequestDto;
import com.flab.delivery.enums.StoreStatus;
import com.flab.delivery.mapper.StoreMapper;
import com.flab.delivery.utils.SessionConstants;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@Transactional
@EnableMockMvc
class StoreControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    @DisplayName("POST : /stores")
    class createStore {
        private Long addressId = 1L;
        private Long categoryId = 1L;
        private String detailAddress = "상세주소201호";
        private String name = "푸라닭";
        private String phoneNumber = "010-1111-1111";
        private String info = "고추마요 맛있습니다";
        private String openTime = "오전10시";
        private String endTime = "오후 12시";
        private Long minPrice = 18000L;

        private String ownerId = "user2";
        private final String url = "/stores";

        private MockHttpSession mockHttpSession = new MockHttpSession();

        private StoreRequestDto getStoreRequestDto() {
            return StoreRequestDto.builder()
                    .addressId(addressId)
                    .categoryId(categoryId)
                    .detailAddress(detailAddress)
                    .name(name)
                    .phoneNumber(phoneNumber)
                    .info(info)
                    .openTime(openTime)
                    .endTime(endTime)
                    .minPrice(minPrice)
                    .build();
        }

        @BeforeEach
        void setUp() {
            mockHttpSession.setAttribute(SessionConstants.SESSION_ID, ownerId);
        }

        @Nested
        @DisplayName("성공")
        class Success {

            @Test
            @DisplayName("매장 생성")
            void createStore_Success() throws Exception {
                // given
                String json = objectMapper.writeValueAsString(getStoreRequestDto());
                // when
                // then
                mockMvc.perform(post(url).session(mockHttpSession)
                                .content(json).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("실패")
        class Fail {

            @Nested
            @DisplayName("세션")
            class fail_session {
                @Test
                @DisplayName("세션 존재x")
                void sessionExists() throws Exception {
                    // given
                    String json = objectMapper.writeValueAsString(getStoreRequestDto());
                    mockHttpSession.removeAttribute(SessionConstants.SESSION_ID);
                    // when
                    // then
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                            .andExpect(jsonPath("$.message").value("세션 아이디가 존재하지 않습니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("권한")
                void userType() throws Exception {
                    // given
                    String json = objectMapper.writeValueAsString(getStoreRequestDto());
                    mockHttpSession.setAttribute(SessionConstants.SESSION_ID, "user1");
                    // when
                    // then
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                            .andExpect(jsonPath("$.message").value("권한이 없습니다"))
                            .andDo(print());
                }
            }

            @Nested
            @DisplayName("주소")
            class fail_address {
                @Test
                @DisplayName("Null인경우")
                void addressNull() throws Exception {
                    // given
                    addressId = null;
                    String json = objectMapper.writeValueAsString(getStoreRequestDto());
                    // when
                    // then
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("주소 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("0인 경우")
                void addressIdZero() throws Exception {
                    // given
                    addressId = 0L;
                    String json = objectMapper.writeValueAsString(getStoreRequestDto());
                    // when
                    // then
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("주소 정보가 올바르지 않습니다"))
                            .andDo(print());
                }
            }

            @Nested
            @DisplayName("카테고리")
            class fail_category {
                @Test
                @DisplayName("Null인경우")
                void categoryNull() throws Exception {
                    // given
                    categoryId = null;
                    String json = objectMapper.writeValueAsString(getStoreRequestDto());
                    // when
                    // then
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("카테고리 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("0인경우")
                void categoryIdZero() throws Exception {
                    // given
                    categoryId = 0L;
                    String json = objectMapper.writeValueAsString(getStoreRequestDto());
                    // when
                    // then
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("카테고리 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

            }

            @Nested
            @DisplayName("상세주소")
            class fail_detailAddress {

                @Test
                @DisplayName("NULL인 경우")
                void detailAddressNull() throws Exception {
                    // given
                    detailAddress = null;
                    String json = objectMapper.writeValueAsString(getStoreRequestDto());
                    // when
                    // then
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("상세 주소 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("공백인 경우")
                void detailAddressEmpty() throws Exception {
                    // given
                    detailAddress = " ";
                    String json = objectMapper.writeValueAsString(getStoreRequestDto());
                    // when
                    // then
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("상세 주소 정보가 올바르지 않습니다"))
                            .andDo(print());
                }
            }

            @Nested
            @DisplayName("매장 이름")
            class fail_name {
                @Test
                @DisplayName("NULL인 경우")
                void nameNull() throws Exception {
                    // given
                    name = null;
                    String json = objectMapper.writeValueAsString(getStoreRequestDto());
                    // when
                    // then
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("매장 이름 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("공백인 경우")
                void nameEmpty() throws Exception {
                    // given
                    name = " ";
                    String json = objectMapper.writeValueAsString(getStoreRequestDto());
                    // when
                    // then
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("매장 이름 정보가 올바르지 않습니다"))
                            .andDo(print());
                }
            }

            @Nested
            @DisplayName("핸드폰 번호")
            class fail_phoneNumber {
                @Test
                @DisplayName("NULL")
                void phoneNumberNull() throws Exception {
                    // given
                    phoneNumber = null;
                    String json = objectMapper.writeValueAsString(getStoreRequestDto());
                    // when
                    // then
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("핸드폰 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("공백")
                void phoneNumberEmpty() throws Exception {
                    // given
                    phoneNumber = " ";
                    String json = objectMapper.writeValueAsString(getStoreRequestDto());
                    // when
                    // then
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("핸드폰 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("형식")
                void phoneNumberForm() throws Exception {
                    //given
                    phoneNumber = "01011111111";
                    String json = objectMapper.writeValueAsString(getStoreRequestDto());
                    // when
                    // then
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("핸드폰 정보가 올바르지 않습니다"))
                            .andDo(print());
                }
            }

            @Nested
            @DisplayName("설명")
            class fail_info {
                @Test
                @DisplayName("NULL인 경우")
                void infoNull() throws Exception {
                    // given
                    info = null;
                    String json = objectMapper.writeValueAsString(getStoreRequestDto());
                    // when
                    // then
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("매장 설명 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("공백인 경우")
                void infoEmpty() throws Exception {
                    // given
                    info = " ";
                    String json = objectMapper.writeValueAsString(getStoreRequestDto());
                    // when
                    // then
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("매장 설명 정보가 올바르지 않습니다"))
                            .andDo(print());
                }
            }

            @Nested
            @DisplayName("오픈시간")
            class fail_openTime {
                @Test
                @DisplayName("NULL인 경우")
                void openTimeNull() throws Exception {
                    // given
                    openTime = null;
                    String json = objectMapper.writeValueAsString(getStoreRequestDto());
                    // when
                    // then
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("오픈 시간 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("공백인 경우")
                void openTimeEmpty() throws Exception {
                    // given
                    openTime = " ";
                    String json = objectMapper.writeValueAsString(getStoreRequestDto());
                    // when
                    // then
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("오픈 시간 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

            }

            @Nested
            @DisplayName("마감시간")
            class fail_endTime {
                @Test
                @DisplayName("NULL인 경우")
                void endTimeNull() throws Exception {
                    // given
                    endTime = null;
                    String json = objectMapper.writeValueAsString(getStoreRequestDto());
                    // when
                    // then
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("마감 시간 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("공백인 경우")
                void endTimeEmpty() throws Exception {
                    // given
                    endTime = " ";
                    String json = objectMapper.writeValueAsString(getStoreRequestDto());
                    // when
                    // then
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("마감 시간 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

            }

            @Nested
            @DisplayName("최소 가격")
            class fail_minPrice {
                @Test
                @DisplayName("0보다 작은경우")
                void minPriceNegative() throws Exception {
                    //given
                    minPrice = -1000L;
                    String json = objectMapper.writeValueAsString(getStoreRequestDto());
                    // when
                    // then
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("최소 주문 가격 정보가 올바르지 않습니다"))
                            .andDo(print());

                }

                @Test
                @DisplayName("Null인 경우")
                void minPriceNull() throws Exception {
                    // given
                    minPrice = null;
                    String json = objectMapper.writeValueAsString(getStoreRequestDto());
                    // when
                    // then
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("최소 주문 가격 정보가 올바르지 않습니다"))
                            .andDo(print());
                }
            }

            @Test
            @DisplayName("중복된 경우")
            void fail_exists() throws Exception {
                // given
                name = "BBQ치킨";
                detailAddress = "매장상세주소 101호";
                String json = objectMapper.writeValueAsString(getStoreRequestDto());
                // when
                mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                        .andExpect(jsonPath("$.message").value("이미 존재하는 매장입니다"))
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("GET : /stores")
    class getOwnerStoreList {

        private String ownerId = "user2";

        private MockHttpSession mockHttpSession = new MockHttpSession();

        private final String url = "/stores";

        @BeforeEach
        void setUp() {
            mockHttpSession.setAttribute(SessionConstants.SESSION_ID, ownerId);
        }

        @Nested
        @DisplayName("성공")
        class Success {
            @Test
            @DisplayName("조회 성공")
            void getOwnerStoreList_Success() throws Exception {
                mockMvc.perform(get(url).session(mockHttpSession))
                        .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                        .andExpect(jsonPath("$.message").value("요청 성공하였습니다."))
                        .andExpect(jsonPath("$.data").isArray())
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("실패")
        class Fail {
            @Test
            @DisplayName("세션 존재x")
            void sessionExists() throws Exception {
                // given
                mockHttpSession.removeAttribute(SessionConstants.SESSION_ID);
                // when
                // then
                mockMvc.perform(get(url).session(mockHttpSession))
                        .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                        .andExpect(jsonPath("$.message").value("세션 아이디가 존재하지 않습니다"))
                        .andDo(print());
            }

            @Test
            @DisplayName("권한")
            void userType() throws Exception {
                // given
                mockHttpSession.setAttribute(SessionConstants.SESSION_ID, "user1");
                // when
                // then
                mockMvc.perform(get(url).session(mockHttpSession))
                        .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                        .andExpect(jsonPath("$.message").value("권한이 없습니다"))
                        .andDo(print());
            }

        }
    }

    @Nested
    @DisplayName("GET : /stores/{id}")
    class getStore {

        private final String ownerId = "user2";

        private MockHttpSession mockHttpSession = new MockHttpSession();

        private final String url = "/stores/1";

        @BeforeEach
        void setUp() {
            mockHttpSession.setAttribute(SessionConstants.SESSION_ID, ownerId);
        }

        @Nested
        @DisplayName("성공")
        class Success {
            @Test
            @DisplayName("상세 조회 성공")
            void getStore_Success() throws Exception {
                mockMvc.perform(get(url).session(mockHttpSession))
                        .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                        .andExpect(jsonPath("$.message").value("요청 성공하였습니다."))
                        .andExpect(jsonPath("$.data").exists())
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("실패")
        class Fail {
            @Test
            @DisplayName("세션 존재x")
            void sessionExists() throws Exception {
                // given
                mockHttpSession.removeAttribute(SessionConstants.SESSION_ID);
                // when
                // then
                mockMvc.perform(get(url).session(mockHttpSession))
                        .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                        .andExpect(jsonPath("$.message").value("세션 아이디가 존재하지 않습니다"))
                        .andDo(print());
            }

            @Test
            @DisplayName("권한")
            void userType() throws Exception {
                // given
                mockHttpSession.setAttribute(SessionConstants.SESSION_ID, "user1");
                // when
                // then
                mockMvc.perform(get(url).session(mockHttpSession))
                        .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                        .andExpect(jsonPath("$.message").value("권한이 없습니다"))
                        .andDo(print());
            }

            @Test
            @DisplayName("존재하지 않는경우")
            void exists() throws Exception {
                String changeUrl = "/stores/1000";
                mockMvc.perform(get(changeUrl).session(mockHttpSession))
                        .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                        .andExpect(jsonPath("$.message").value("존재하지 않는 매장입니다"))
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("PUT : /stores/{id}")
    class updateStore {


        private Long addressId = 1L;
        private Long categoryId = 1L;
        private String detailAddress = "상세주소201호";
        private String name = "푸라닭";
        private String phoneNumber = "010-1111-1111";
        private String info = "고추마요 맛있습니다";
        private String openTime = "오전10시";
        private String endTime = "오후 12시";
        private Long minPrice = 18000L;

        private final String ownerId = "user2";

        private MockHttpSession mockHttpSession = new MockHttpSession();

        private final String url = "/stores/1";

        private StoreRequestDto getStoreRequestDto() {
            return StoreRequestDto.builder()
                    .addressId(addressId)
                    .categoryId(categoryId)
                    .detailAddress(detailAddress)
                    .name(name)
                    .phoneNumber(phoneNumber)
                    .info(info)
                    .openTime(openTime)
                    .endTime(endTime)
                    .minPrice(minPrice)
                    .build();
        }

        @BeforeEach
        void setUp() {
            mockHttpSession.setAttribute(SessionConstants.SESSION_ID, ownerId);
        }

        @Nested
        @DisplayName("성공")
        class Success {

            @Test
            @DisplayName("업데이트 성공")
            void updateStore_Success(@Autowired StoreMapper storeMapper) throws Exception {
                // given
                Long id = 1L;
                Long beforePrice = storeMapper.findById(id).get().getMinPrice();
                minPrice = 20000L;
                String json = objectMapper.writeValueAsString(getStoreRequestDto());
                // when
                mockMvc.perform(put(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                        .andDo(print());
                Long afterPrice = storeMapper.findById(id).get().getMinPrice();
                // then
                Assertions.assertNotEquals(beforePrice, afterPrice);
            }
        }

        @Nested
        @DisplayName("실패")
        class Fail {
            // 세션정보불일치(세션x, 권한안맞는경우)
            // 존재하지않는경우
            // null인경우
            @Test
            @DisplayName("세션 존재x")
            void sessionExists() throws Exception {
                // given
                mockHttpSession.removeAttribute(SessionConstants.SESSION_ID);
                String json = objectMapper.writeValueAsString(getStoreRequestDto());
                // when
                // then
                mockMvc.perform(put(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                        .andExpect(jsonPath("$.message").value("세션 아이디가 존재하지 않습니다"))
                        .andDo(print());
            }

            @Test
            @DisplayName("권한")
            void userType() throws Exception {
                // given
                String json = objectMapper.writeValueAsString(getStoreRequestDto());
                mockHttpSession.setAttribute(SessionConstants.SESSION_ID, "user1");
                // when
                // then
                mockMvc.perform(put(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                        .andExpect(jsonPath("$.message").value("권한이 없습니다"))
                        .andDo(print());
            }

            @Test
            @DisplayName("존재하지 않는경우")
            void exists() throws Exception {
                //given
                String changeUrl = "/stores/1000";
                String json = objectMapper.writeValueAsString(getStoreRequestDto());
                mockMvc.perform(put(changeUrl).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                        .andExpect(jsonPath("$.message").value("존재하지 않는 매장입니다"))
                        .andDo(print());
            }

            @Test
            @DisplayName("입력 정보-가격")
            void inputValid() throws Exception {
                // given
                minPrice = -100L;
                String json = objectMapper.writeValueAsString(getStoreRequestDto());
                mockMvc.perform(put(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                        .andExpect(jsonPath("$.message").value("최소 주문 가격 정보가 올바르지 않습니다"))
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("DELETE : /stores/{id}")
    class deleteStore {
        private final String ownerId = "user2";
        private final String url = "/stores/1";
        private MockHttpSession mockHttpSession = new MockHttpSession();

        @BeforeEach
        void setUp() {
            mockHttpSession.setAttribute(SessionConstants.SESSION_ID, ownerId);
        }

        @Nested
        @DisplayName("성공")
        class Success {
            @Test
            @DisplayName("삭제 성공")
            void deleteStore_Success(@Autowired StoreMapper storeMapper) throws Exception {
                Long id = 1L;
                mockMvc.perform(delete(url).session(mockHttpSession))
                        .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                        .andDo(print());
                Assertions.assertFalse(storeMapper.findById(id).isPresent());
            }
        }

        @Nested
        @DisplayName("실패")
        class Fail {
            @Test
            @DisplayName("세션 존재x")
            void sessionExists() throws Exception {
                // given
                mockHttpSession.removeAttribute(SessionConstants.SESSION_ID);
                // when
                // then
                mockMvc.perform(delete(url).session(mockHttpSession))
                        .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                        .andExpect(jsonPath("$.message").value("세션 아이디가 존재하지 않습니다"))
                        .andDo(print());
            }

            @Test
            @DisplayName("권한")
            void userType() throws Exception {
                // given
                mockHttpSession.setAttribute(SessionConstants.SESSION_ID, "user1");
                // when
                // then
                mockMvc.perform(delete(url).session(mockHttpSession))
                        .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                        .andExpect(jsonPath("$.message").value("권한이 없습니다"))
                        .andDo(print());
            }

            @Test
            @DisplayName("존재하지 않는경우")
            void exists() throws Exception {
                //given
                String changeUrl = "/stores/1000";
                mockMvc.perform(delete(changeUrl).session(mockHttpSession))
                        .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                        .andExpect(jsonPath("$.message").value("존재하지 않는 매장입니다"))
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("PUT : /stores/{id}/status")
    class changeStatus {

        private final String ownerId = "user2";
        private final String url = "/stores/1/status";
        private MockHttpSession mockHttpSession = new MockHttpSession();

        StoreDto getStoreDto(StoreStatus status) {
            return StoreDto.builder().status(status).build();
        }

        @BeforeEach
        void setUp() {
            mockHttpSession.setAttribute(SessionConstants.SESSION_ID, ownerId);
        }


        @Nested
        @DisplayName("성공")
        class Success {
            @Test
            @DisplayName("상태 변경 CLOSED -> OPEN")
            void changeStatus_Success(@Autowired StoreMapper storeMapper) throws Exception {
                // given
                Long id = 1L;
                StoreStatus beforeStatus = storeMapper.findById(id).get().getStatus();
                StoreStatus changeStatus = StoreStatus.OPEN;
                String json = objectMapper.writeValueAsString(getStoreDto(changeStatus));
                // when
                mockMvc.perform(put(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                        .andDo(print());
                StoreStatus afterStatus = storeMapper.findById(id).get().getStatus();
                // then
                Assertions.assertNotEquals(beforeStatus, afterStatus);
            }
        }

        @Nested
        @DisplayName("실패")
        class Fail {
            @Test
            @DisplayName("세션 존재x")
            void sessionExists() throws Exception {
                // given
                mockHttpSession.removeAttribute(SessionConstants.SESSION_ID);
                String json = objectMapper.writeValueAsString(getStoreDto(StoreStatus.OPEN));
                // when
                // then
                mockMvc.perform(put(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                        .andExpect(jsonPath("$.message").value("세션 아이디가 존재하지 않습니다"))
                        .andDo(print());
            }

            @Test
            @DisplayName("권한")
            void userType() throws Exception {
                // given
                mockHttpSession.setAttribute(SessionConstants.SESSION_ID, "user1");
                String json = objectMapper.writeValueAsString(getStoreDto(StoreStatus.OPEN));
                // when
                // then
                mockMvc.perform(put(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                        .andExpect(jsonPath("$.message").value("권한이 없습니다"))
                        .andDo(print());
            }

            @Test
            @DisplayName("존재하지 않는경우")
            void exists() throws Exception {
                //given
                String changeUrl = "/stores/1000/status";
                String json = objectMapper.writeValueAsString(getStoreDto(StoreStatus.OPEN));
                // when
                // then
                mockMvc.perform(put(changeUrl).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                        .andExpect(jsonPath("$.message").value("존재하지 않는 매장입니다"))
                        .andDo(print());
            }
        }
    }
}