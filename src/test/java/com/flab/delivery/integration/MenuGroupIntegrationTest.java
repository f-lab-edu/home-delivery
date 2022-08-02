package com.flab.delivery.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.delivery.annotation.EnableMockMvc;
import com.flab.delivery.dto.menu.MenuGroupDto;
import com.flab.delivery.dto.menu.MenuGroupRequestDto;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.mapper.MenuGroupMapper;
import com.flab.delivery.utils.SessionConstants;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@EnableMockMvc
@Transactional
class MenuGroupIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    @DisplayName("POST : /menugroups")
    class createMenuGroup {

        private final String ownerId = "user2";
        private final UserType userType = UserType.OWNER;
        private final String url = "/menugroups";


        private Long storeId = 1L;
        private String name = "치킨";
        private String info = "맛있는 치킨입니다";

        private MenuGroupRequestDto getRequestDto() {
            return MenuGroupRequestDto.builder()
                    .storeId(storeId)
                    .name(name)
                    .info(info)
                    .build();
        }

        private MockHttpSession mockHttpSession = new MockHttpSession();

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
                // given
                String json = objectMapper.writeValueAsString(getRequestDto());
                // when
                // then
                mockMvc.perform(post(url).session(mockHttpSession).content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("실패")
        class Fail {
            @Nested
            @DisplayName("매장 ID")
            class StoreId {
                @Test
                @DisplayName("NULL인 경우")
                void storeIdNull() throws Exception {
                    // given
                    storeId = null;
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("매장 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("음수인 경우")
                void storeIdNegative() throws Exception {
                    // given
                    storeId = -1L;
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("매장 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("공백인 경우")
                void storeIdBlank() throws Exception {
                    // given
                    storeId = Long.getLong(" ");
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("매장 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("존재하지않는경우")
                void storeIdNotExists() throws Exception {
                    // given
                    storeId = 3L;
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("StoreId가 존재하지않아 무결성 제약 조건에 위배됩니다"))
                            .andDo(print());
                }
            }

            @Nested
            @DisplayName("세션")
            class Session {
                @Test
                @DisplayName("권한")
                void userType() throws Exception {
                    // given
                    UserType changeType = UserType.USER;
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, changeType);
                    // when
                    // then
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                            .andExpect(jsonPath("$.message").value("권한이 없습니다"))
                            .andDo(print());
                }
            }

            @Nested
            @DisplayName("이름")
            class Name {
                @Test
                @DisplayName("NULL인 경우")
                void nameNull() throws Exception {
                    // given
                    name = null;
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("메뉴그룹 이름 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("공백 경우")
                void nameBlank() throws Exception {
                    // given
                    name = " ";
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("메뉴그룹 이름 정보가 올바르지 않습니다"))
                            .andDo(print());
                }
            }

            @Nested
            @DisplayName("정보")
            class Info {
                @Test
                @DisplayName("NULL인 경우")
                void infoNull() throws Exception {
                    // given
                    info = null;
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("메뉴그룹 설명 정보가 올바르지 않습니다"))
                            .andDo(print());
                }

                @Test
                @DisplayName("공백 경우")
                void infoBlank() throws Exception {
                    // given
                    info = " ";
                    String json = objectMapper.writeValueAsString(getRequestDto());
                    mockMvc.perform(post(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.message").value("메뉴그룹 설명 정보가 올바르지 않습니다"))
                            .andDo(print());
                }
            }
        }

    }

    @Nested
    @DisplayName("PATCH : /menugroups/{id}")
    class updateMenuGroup {
        private final String ownerId = "user2";
        private final UserType userType = UserType.OWNER;
        private final String url = "/menugroups/1";


        private Long storeId = 1L;
        private String name = "매운 족발";
        private String info = "매운 족발입니다";

        private MenuGroupRequestDto getRequestDto() {
            return MenuGroupRequestDto.builder()
                    .storeId(storeId)
                    .name(name)
                    .info(info)
                    .build();
        }

        private MockHttpSession mockHttpSession = new MockHttpSession();

        @BeforeEach
        void setUp() {
            mockHttpSession.setAttribute(SessionConstants.SESSION_ID, ownerId);
            mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, userType);
        }

        @Nested
        @DisplayName("성공")
        class Success {
            @Test
            @DisplayName("정보 변경 성공")
            void success(@Autowired MenuGroupMapper menuGroupMapper) throws Exception {
                // given
                MenuGroupDto before = menuGroupMapper.findById(1L).get();
                String json = objectMapper.writeValueAsString(getRequestDto());
                mockMvc.perform(patch(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                        .andDo(print());
                MenuGroupDto after = menuGroupMapper.findById(1L).get();
                Assertions.assertNotEquals(before.getName(), after.getName());
            }
        }

        @Nested
        @DisplayName("실패")
        class Fail {
            @Test
            @DisplayName("존재 안하는 경우")
            void notExists() throws Exception {
                // given
                String changeUrl = "/menugroups/100";
                String json = objectMapper.writeValueAsString(getRequestDto());
                // when
                mockMvc.perform(patch(changeUrl).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                        .andExpect(jsonPath("$.message").value("존재하지 않는 메뉴 그룹입니다"))
                        .andDo(print());
            }

            @Test
            @DisplayName("권한")
            void userType() throws Exception {
                // given
                UserType changeUserType = UserType.USER;
                mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, changeUserType);
                String json = objectMapper.writeValueAsString(getRequestDto());
                // when
                mockMvc.perform(patch(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                        .andExpect(jsonPath("$.message").value("권한이 없습니다"))
                        .andDo(print());
            }

            /**
             * valid체크는 post요청시에도 이루어지기때문에 동일한 체크라고생각해서
             * url만 다르기때문에 patch에서는 한가지부분만 valid가 적용되는지 체크하였습니다
             */
            @Test
            @DisplayName("storeId valid체크")
            void storeIdNegative() throws Exception {
                // given
                storeId = -1L;
                String json = objectMapper.writeValueAsString(getRequestDto());
                mockMvc.perform(patch(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                        .andExpect(jsonPath("$.message").value("매장 정보가 올바르지 않습니다"))
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("GET : /menugroups/storeid/{id}")
    class getMenuGroupList {
        private final String ownerId = "user2";
        private final UserType userType = UserType.OWNER;
        private final String url = "/menugroups/storeid/1";

        private MockHttpSession mockHttpSession = new MockHttpSession();

        @BeforeEach
        void setUp() {
            mockHttpSession.setAttribute(SessionConstants.SESSION_ID, ownerId);
            mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, userType);
        }

        @Test
        @DisplayName("조회 성공")
        void success() throws Exception {
            // given
            mockMvc.perform(get(url).session(mockHttpSession))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.data").isArray())
                    .andDo(print());
        }


    }

    @Nested
    @DisplayName("DELETE : /menugroups/{id}")
    class deleteGroup {
        private final String ownerId = "user2";
        private final UserType userType = UserType.OWNER;
        private final String url = "/menugroups/1";

        private MockHttpSession mockHttpSession = new MockHttpSession();

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
            void deleteGroup_Success(@Autowired MenuGroupMapper menuGroupMapper) throws Exception {
                // given
                Long deleteId = 1L;
                // when
                mockMvc.perform(delete(url).session(mockHttpSession))
                        .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                        .andDo(print());
                // then
                Assertions.assertFalse(menuGroupMapper.findById(deleteId).isPresent());
            }

        }

        @Nested
        @DisplayName("실패")
        class Fail {
            @Test
            @DisplayName("존재하지 않는 경우")
            void notExists() throws Exception {
                // given
                String changeUrl = "/menugroups/100";
                // when
                // then
                mockMvc.perform(delete(changeUrl).session(mockHttpSession))
                        .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                        .andExpect(jsonPath("$.message").value("존재하지 않는 메뉴 그룹입니다"))
                        .andDo(print());
            }

            @Test
            @DisplayName("권한이 없는 경우")
            void userType() throws Exception {
                // given
                UserType changeType = UserType.USER;
                mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, changeType);
                // when
                // then
                mockMvc.perform(delete(url).session(mockHttpSession))
                        .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                        .andExpect(jsonPath("$.message").value("권한이 없습니다"))
                        .andDo(print());
            }

        }


    }

    @Nested
    @DisplayName("PATCH : /menugroups/priorities")
    class changePriority {
        private final String ownerId = "user2";
        private final UserType userType = UserType.OWNER;
        private final String url = "/menugroups/priorities";

        private MockHttpSession mockHttpSession = new MockHttpSession();


        private List<MenuGroupDto> list = new ArrayList<>();

        @BeforeEach
        void setUp() {
            mockHttpSession.setAttribute(SessionConstants.SESSION_ID, ownerId);
            mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, userType);

            for (int i = 1; i <= 5; i++) {
                list.add(getMenuGroupDto((long) i, i));
            }
        }

        private MenuGroupDto getMenuGroupDto(Long id, Integer priority) {
            return MenuGroupDto.builder()
                    .id(id)
                    .priority(priority)
                    .build();
        }

        @Test
        @DisplayName("우선순위 변경 성공")
        void success() throws Exception {
            MenuGroupDto requestDto = MenuGroupDto.builder()
                    .menuGroupDtoList(list)
                    .build();
            String json = objectMapper.writeValueAsString(requestDto);
            mockMvc.perform(patch(url).session(mockHttpSession).content(json).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andDo(print());
        }
    }

}