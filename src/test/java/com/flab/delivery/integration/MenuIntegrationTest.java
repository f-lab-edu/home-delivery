package com.flab.delivery.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.delivery.annotation.IntegrationTest;
import com.flab.delivery.dto.menu.MenuDto;
import com.flab.delivery.dto.menu.MenuRequestDto;
import com.flab.delivery.enums.MenuStatus;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.mapper.MenuMapper;
import com.flab.delivery.utils.SessionConstants;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.flab.delivery.exception.message.ErrorMessageConstants.FORBIDDEN_MESSAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@IntegrationTest
class MenuIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    MockHttpSession mockHttpSession = new MockHttpSession();

    @Nested
    @DisplayName("POST : /menus")
    class CreateMenu {
        private final String ownerId = "user2";
        private final UserType userType = UserType.OWNER;
        private final String url = "/menus";

        private Long menuGroupId = 1L;
        private String name = "민트초코치킨";
        private String info = "올리브유에 튀킨 치킨";
        private Integer price = 18000;

        private MenuRequestDto getRequestDto() {
            return MenuRequestDto.builder()
                    .menuGroupId(menuGroupId)
                    .name(name)
                    .info(info)
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
            @DisplayName("메뉴그룹")
            class MenuGroupId {
                @Test
                @DisplayName("NULL인경우")
                void Null() throws Exception {
                    menuGroupId = null;
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("매뉴 그룹 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("음수인 경우")
                void negative() throws Exception {
                    menuGroupId = -1L;
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("매뉴 그룹 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("공백인 경우")
                void blank() throws Exception {
                    menuGroupId = Long.getLong(" ");
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("매뉴 그룹 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("존재하지않는경우")
                void notExists() throws Exception {
                    // given
                    menuGroupId = 100L;
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("무결성 제약 조건에 위배됩니다"))
                            .andDo(print());
                }
            }

            @Nested
            @DisplayName("메뉴 이름")
            class Name {
                @Test
                @DisplayName("NULL인경우")
                void isNull() throws Exception {
                    name = null;
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("메뉴 이름 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("공백인 경우")
                void blank() throws Exception {
                    name = " ";
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("메뉴 이름 정보가 올바르지 않습니다"))
                            .andDo(print());
                }


            }


            @Nested
            @DisplayName("설명")
            class Info {
                @Test
                @DisplayName("Null인 경우")
                void isNull() throws Exception {
                    info = null;
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("메뉴 설명 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("공백인 경우")
                void blank() throws Exception {
                    info = " ";
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("메뉴 설명 정보가 올바르지 않습니다"))
                            .andDo(print());
                }
            }

            @Nested
            @DisplayName("가격")
            class Price {

                @Test
                @DisplayName("Null인 경우")
                void isNull() throws Exception {
                    price = null;
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("메뉴 가격 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("음수인 경우")
                void negative() throws Exception {
                    price = -1;
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("메뉴 가격 정보가 올바르지 않습니다"))
                            .andDo(print());
                }
            }

            @Nested
            @DisplayName("세션")
            class Session {
                @Test
                @DisplayName("권한")
                void userType() throws Exception {
                    UserType changeType = UserType.USER;
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, changeType);

                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                            .andExpect(jsonPath("$.message").value(FORBIDDEN_MESSAGE))
                            .andDo(print());
                }
            }
        }


    }

    @Nested
    @DisplayName("GET : /menus/{id}")
    class GetMenu {
        private final String ownerId = "user2";
        private final UserType userType = UserType.OWNER;
        private final String url = "/menus/1";

        @BeforeEach
        void setUp() {
            mockHttpSession.setAttribute(SessionConstants.SESSION_ID, ownerId);
            mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, userType);
        }

        @Nested
        @DisplayName("성공")
        class Success {
            @Test
            @DisplayName("조회 성공")
            void success() throws Exception {
                mockMvc.perform(get(url).session(mockHttpSession))
                        .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                        .andExpect(jsonPath("$.data.name").value("후라이드 치킨"))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("실패")
        class Fail {

            @Test
            @DisplayName("존재하지 않는경우")
            void notExists() throws Exception {
                String changeUrl = "/menus/1000";

                mockMvc.perform(get(changeUrl).session(mockHttpSession))
                        .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                        .andExpect(jsonPath("$.message").value("존재하지 않는 메뉴입니다"))
                        .andDo(print());
            }
        }
    }


    @Nested
    @DisplayName("PATCH : /menus/{id}")
    class UpdateMenu {
        private final String ownerId = "user2";
        private final UserType userType = UserType.OWNER;
        private final String url = "/menus/1";

        private Long menuGroupId = 1L;
        private String name = "민트초코치킨";
        private String info = "민츠초코 좋아요";
        private Integer price = 30000;

        @BeforeEach
        void setUp() {
            mockHttpSession.setAttribute(SessionConstants.SESSION_ID, ownerId);
            mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, userType);
        }

        private MenuRequestDto getRequestDto() {
            return MenuRequestDto.builder()
                    .menuGroupId(menuGroupId)
                    .name(name)
                    .info(info)
                    .price(price)
                    .build();
        }

        @Nested
        @DisplayName("성공")
        class Success {

            @Test
            @DisplayName("후라이드치킨 -> 민트초코치킨")
            void success(@Autowired MenuMapper menuMapper) throws Exception {
                String json = objectMapper.writeValueAsString(getRequestDto());
                Long findId = 1L;
                MenuDto before = menuMapper.findById(findId).get();
                System.out.println("before" + before.getName());

                mockMvc.perform(patch(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                        .andDo(print());

                MenuDto after = menuMapper.findById(findId).get();

                Assertions.assertNotEquals(before.getName(), after.getName());
            }
        }

        @Nested
        @DisplayName("실패")
        class Fail {

            @Test
            @DisplayName("권한")
            void userType() throws Exception {
                UserType changeType = UserType.USER;
                String json = objectMapper.writeValueAsString(getRequestDto());

                mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, changeType);
                mockMvc.perform(patch(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                        .andExpect(jsonPath("$.message").value(FORBIDDEN_MESSAGE))
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("DLETE : /menus/{id}")
    class DeleteMenu {
        private final String ownerId = "user2";
        private final UserType userType = UserType.OWNER;
        private final String url = "/menus/1";

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
            void success(@Autowired MenuMapper menuMapper) throws Exception {
                Long deleteId = 1L;
                mockMvc.perform(delete(url).session(mockHttpSession))
                        .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                        .andDo(print());

                Optional<MenuDto> deleteMenu = menuMapper.findById(deleteId);
                Assertions.assertFalse(deleteMenu.isPresent());
            }
        }

        @Nested
        @DisplayName("실패")
        class Fail {

            @Test
            @DisplayName("권한")
            void userType() throws Exception {
                UserType changeType = UserType.USER;

                mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, changeType);
                mockMvc.perform(delete(url).session(mockHttpSession))
                        .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                        .andExpect(jsonPath("$.message").value(FORBIDDEN_MESSAGE))
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("PATCH : /menus/{id}/status")
    class UpdateStatus {
        private final String ownerId = "user2";
        private final UserType userType = UserType.OWNER;
        private final String url = "/menus/1/status";

        private MenuStatus menuStatus = MenuStatus.SOLDOUT;

        @BeforeEach
        void setUp() {
            mockHttpSession.setAttribute(SessionConstants.SESSION_ID, ownerId);
            mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, userType);
        }

        private MenuDto getRequestDto() {
            return MenuDto.builder()
                    .status(menuStatus)
                    .build();
        }

        @Nested
        @DisplayName("성공")
        class Success {
            @Test
            @DisplayName("상태변경 ONSALE -> SOLDOUT")
            void success(@Autowired MenuMapper menuMapper) throws Exception {
                String json = objectMapper.writeValueAsString(getRequestDto());
                Long updateId = 1L;

                MenuDto before = menuMapper.findById(updateId).get();
                mockMvc.perform(patch(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                        .andDo(print());
                MenuDto after = menuMapper.findById(updateId).get();

                Assertions.assertNotEquals(before.getStatus(), after.getStatus());
            }
        }

        @Nested
        @DisplayName("실패")
        class Fail {
            @Test
            @DisplayName("권한이 없는 경우")
            void userType() throws Exception {
                UserType changeType = UserType.USER;
                String json = objectMapper.writeValueAsString(getRequestDto());

                mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, changeType);

                mockMvc.perform(patch(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                        .andExpect(jsonPath("$.message").value(FORBIDDEN_MESSAGE))
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("PATCH : /menus/priorities")
    class UpdatePriority {
        private final String ownerId = "user2";
        private final UserType userType = UserType.OWNER;
        private final String url = "/menus/priorities";

        private List<MenuDto> list = new ArrayList<>();

        @BeforeEach
        void setUp() {
            mockHttpSession.setAttribute(SessionConstants.SESSION_ID, ownerId);
            mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, userType);

            list.add(MenuDto.builder().id(3L).build());
            list.add(MenuDto.builder().id(1L).build());
            list.add(MenuDto.builder().id(2L).build());
        }

        @Nested
        @DisplayName("성공")
        class Success {
            @Test
            @DisplayName("우선순위 변경 성공 - id:3 - 1순위, id:1 - 2순위, id:2 - 3순위")
            void success(@Autowired MenuMapper menuMapper) throws Exception {
                String json = objectMapper.writeValueAsString(list);
                Long peekId = 1L;

                MenuDto before = menuMapper.findById(peekId).get();

                mockMvc.perform(patch(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                        .andDo(print());

                MenuDto after = menuMapper.findById(peekId).get();
                Assertions.assertNotEquals(before.getPriority(), after.getPriority());
            }

        }

        @Nested
        @DisplayName("실패")
        class Fail {
            @Test
            @DisplayName("존재하지 않는 메뉴가 있는경우")
            void notExists() throws Exception {
                Long notExistsId = 100L;
                list.add(MenuDto.builder().id(notExistsId).build());
                String json = objectMapper.writeValueAsString(list);

                mockMvc.perform(patch(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                        .andExpect(jsonPath("$.message").value("업데이트 중 존재하지 않는 id가 있습니다"))
                        .andDo(print());
            }

            @Test
            @DisplayName("권한")
            void userType() throws Exception {
                UserType changeType = UserType.USER;
                String json = objectMapper.writeValueAsString(list);

                mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, changeType);

                mockMvc.perform(patch(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                        .andExpect(jsonPath("$.message").value(FORBIDDEN_MESSAGE))
                        .andDo(print());
            }
        }


    }

    @Nested
    @DisplayName("GET : /menus?storeId={storeId}")
    class GetMenuList {
        private final String ownerId = "user2";
        private final UserType userType = UserType.OWNER;
        private final String url = "/menus";
        private final String paramName = "storeId";
        private final String paramValue = "2";

        @BeforeEach
        void setUp() {
            mockHttpSession.setAttribute(SessionConstants.SESSION_ID, ownerId);
            mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, userType);
        }

        @Nested
        @DisplayName("성공")
        class Success {
            @Test
            @DisplayName("조회 성공")
            void success() throws Exception {
                mockMvc.perform(get(url).param(paramName, paramValue).session(mockHttpSession))
                        .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                        .andExpect(jsonPath("$.data").isArray())
                        .andDo(print());
            }
        }

    }
}