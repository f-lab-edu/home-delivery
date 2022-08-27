package com.flab.delivery.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.delivery.annotation.IntegrationTest;
import com.flab.delivery.dto.cart.ItemDto;
import com.flab.delivery.dto.menu.MenuDto;
import com.flab.delivery.dto.option.OptionDto;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.fixture.MessageConstants;
import com.flab.delivery.mapper.MenuMapper;
import com.flab.delivery.mapper.OptionMapper;
import com.flab.delivery.utils.SessionConstants;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@IntegrationTest
@Testcontainers
@ContextConfiguration(initializers = CartIntegrationTest.ContainerPropertyInitializer.class)
class CartIntegrationTest {

    @Autowired
    MenuMapper menuMapper;

    @Autowired
    OptionMapper optionMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RedisTemplate<String, Object> redisCartTemplate;


    MockHttpSession mockHttpSession = new MockHttpSession();

    @Container
    static GenericContainer redisContainer = new GenericContainer("redis")
            .withExposedPorts(6379);

    static class ContainerPropertyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext context) {
            TestPropertyValues.of("spring.redis.cart.port=" + redisContainer.getMappedPort(6379))
                    .applyTo(context.getEnvironment());
        }
    }




    @Nested
    @DisplayName("POST : /carts?storeId={storeId}")
    class InsertOne {

        private String url = "/carts";

        @AfterEach
        void afterEach() {
            redisCartTemplate.delete("user1");
            redisCartTemplate.delete("STORE_ID");
        }

        @Test
        @DisplayName("CartType : FIRST - 한번 넣는 경우")
        public void insertItemFirst() throws Exception {
            mockHttpSession.setAttribute(SessionConstants.SESSION_ID, "user1");
            mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, UserType.USER);

            List<OptionDto> optionDtoList = optionMapper.findAllByMenuId(1L);
            MenuDto menuDto = menuMapper.findById(1L).get();
            ItemDto itemDto = new ItemDto(menuDto, optionDtoList);
            String json = objectMapper.writeValueAsString(itemDto);

            mockMvc.perform(post(url).session(mockHttpSession).param("storeId", "1")
                            .content(json).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                    .andDo(print());

            mockMvc.perform(get(url).session(mockHttpSession))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.message").value(MessageConstants.SUCCESS_MESSAGE))
                    .andExpect(jsonPath("$.data.cartList.[0]").exists())
                    .andDo(print());
        }


    }


    @Nested
    @DisplayName("POST : /carts?storeId={storeId} 2번 - 물건이있는데 담는 경우")
    class InsertItemTwo {

        private String url = "/carts";
        private Long menuId = 2L;
        private String storeId = "1";

        @BeforeEach
        void insertItemFirst() throws Exception {
            mockHttpSession.setAttribute(SessionConstants.SESSION_ID, "user1");
            mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, UserType.USER);

            List<OptionDto> optionDtoList = optionMapper.findAllByMenuId(menuId);
            MenuDto menuDto = menuMapper.findById(menuId).get();
            ItemDto itemDto = new ItemDto(menuDto, optionDtoList);

            String json = objectMapper.writeValueAsString(itemDto);

            mockMvc.perform(post(url).session(mockHttpSession).param("storeId", storeId)
                            .content(json).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                    .andDo(print());
        }

        @AfterEach
        void afterEach() {
            redisCartTemplate.delete("user1");
            redisCartTemplate.delete("STORE_ID");
        }

        @Test
        @DisplayName("같은메뉴 담는경우 - 수량 증가")
        void sameInsert() throws Exception {
            List<OptionDto> optionDtoList = optionMapper.findAllByMenuId(menuId);
            MenuDto menuDto = menuMapper.findById(menuId).get();
            ItemDto itemDto = new ItemDto(menuDto, optionDtoList);

            String json = objectMapper.writeValueAsString(itemDto);

            mockMvc.perform(post(url).session(mockHttpSession).param("storeId", storeId)
                            .content(json).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                    .andDo(print());


            mockMvc.perform(get(url).session(mockHttpSession))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.message").value(MessageConstants.SUCCESS_MESSAGE))
                    .andExpect(jsonPath("$.data.cartList.[0].quantity").value(2))
                    .andDo(print());
        }


        @Test
        @DisplayName("옵션 다른경우  새로운 필드 추가")
        void optionDiff() throws Exception {
            List<OptionDto> optionDtoList = optionMapper.findAllByMenuId(menuId);

            optionDtoList.remove(0);

            MenuDto menuDto = menuMapper.findById(menuId).get();
            ItemDto itemDto = new ItemDto(menuDto, optionDtoList);

            String json = objectMapper.writeValueAsString(itemDto);

            mockMvc.perform(post(url).session(mockHttpSession).param("storeId", storeId)
                            .content(json).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                    .andDo(print());

            mockMvc.perform(get(url).session(mockHttpSession))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.message").value(MessageConstants.SUCCESS_MESSAGE))
                    .andExpect(jsonPath("$.data.cartList.[1]").exists())
                    .andDo(print());
        }

        @Test
        @DisplayName("메뉴 다른경우  새로운 필드 추가")
        void menuDiff() throws Exception {
            Long diffMenuId = 16L;
            List<OptionDto> optionDtoList = optionMapper.findAllByMenuId(diffMenuId);

            MenuDto menuDto = menuMapper.findById(diffMenuId).get();
            ItemDto itemDto = new ItemDto(menuDto, optionDtoList);

            String json = objectMapper.writeValueAsString(itemDto);

            mockMvc.perform(post("/carts").session(mockHttpSession).param("storeId", storeId)
                            .content(json).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                    .andDo(print());

            mockMvc.perform(get(url).session(mockHttpSession))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.message").value(MessageConstants.SUCCESS_MESSAGE))
                    .andExpect(jsonPath("$.data.cartList.[1]").exists())
                    .andDo(print());
        }

        @Test
        @DisplayName("장바구니 다 지우고 새로운것만 존재")
        void storeCartTypeDIFF() throws Exception {
            Long diffStoreMenuId = 16L;
            String diffStore = "2";
            List<OptionDto> optionDtoList = optionMapper.findAllByMenuId(diffStoreMenuId);
            MenuDto menuDto = menuMapper.findById(diffStoreMenuId).get();
            ItemDto itemDto = new ItemDto(menuDto, optionDtoList);

            String json = objectMapper.writeValueAsString(itemDto);

            mockMvc.perform(post("/carts").session(mockHttpSession).param("storeId", diffStore)
                            .content(json).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                    .andDo(print());

            mockMvc.perform(get(url).session(mockHttpSession))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.message").value(MessageConstants.SUCCESS_MESSAGE))
                    .andExpect(jsonPath("$.data.storeId").value(diffStore))
                    .andExpect(jsonPath("$.data.cartList.[0]").exists())
                    .andDo(print());
        }

    }


    @Nested
    @DisplayName("DELETE : /carts/{cartKey}")
    class DeleteItem {
        private String url = "/carts";
        private String pathValue;
        private String userId = "user1";

        private Long menuId = 2L;
        private String storeId = "1";

        private List<OptionDto> optionDtoList = optionMapper.findAllByMenuId(menuId);
        private MenuDto menuDto = menuMapper.findById(menuId).get();

        @BeforeEach
        void setUp() throws Exception {
            mockHttpSession.setAttribute(SessionConstants.SESSION_ID, userId);
            mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, UserType.USER);

            ItemDto itemDto = new ItemDto(menuDto, optionDtoList);

            String json = objectMapper.writeValueAsString(itemDto);

            mockMvc.perform(post("/carts").session(mockHttpSession).param("storeId", storeId)
                            .content(json).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                    .andDo(print());
        }

        @AfterEach
        void afterEach() {
            redisCartTemplate.delete("user1");
            redisCartTemplate.delete("STORE_ID");
        }

        @Test
        @DisplayName("삭제후 다른메뉴 남아있는경우")
        void cartExists() throws Exception {
            // given
            optionDtoList.remove(0);
            ItemDto itemDto = new ItemDto(menuDto, optionDtoList);

            String json = objectMapper.writeValueAsString(itemDto);

            mockMvc.perform(post("/carts").session(mockHttpSession).param("storeId", storeId)
                            .content(json).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                    .andDo(print());


            pathValue = "/2_7_8";
            mockMvc.perform(delete(url + pathValue).session(mockHttpSession))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andDo(print());

            Object getStoreId = redisCartTemplate.opsForHash().get("STORE_ID", userId);
            Assertions.assertNotNull(getStoreId);
        }

        @Test
        @DisplayName("삭제후 장바구니 비어있는경우")
        void cartEmpty() throws Exception {
            pathValue = "/2_7_8";
            mockMvc.perform(delete(url + pathValue).session(mockHttpSession))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andDo(print());

            Object getStoreId = redisCartTemplate.opsForHash().get("STORE_ID", userId);
            Assertions.assertNull(getStoreId);
        }
    }

    @Nested
    @DisplayName("PATCH : /carts/{cartKey}?opertioan={opertionType}")
    class UpdateQuantity {
        private String url = "/carts";
        private String userId = "user1";
        private String pathValue = "/2_7_8";

        private String paramName = "operation";
        private String paramValue;

        private Long menuId = 2L;
        private String storeId = "1";

        @BeforeEach
        void setUp() throws Exception {
            mockHttpSession.setAttribute(SessionConstants.SESSION_ID, userId);
            mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, UserType.USER);

            List<OptionDto> optionDtoList = optionMapper.findAllByMenuId(menuId);
            MenuDto menuDto = menuMapper.findById(menuId).get();

            ItemDto itemDto = new ItemDto(menuDto, optionDtoList);

            String json = objectMapper.writeValueAsString(itemDto);

            mockMvc.perform(post("/carts").session(mockHttpSession).param("storeId", storeId)
                            .content(json).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                    .andDo(print());
        }

        @AfterEach
        void afterEach() {
            redisCartTemplate.delete("user1");
            redisCartTemplate.delete("STORE_ID");
        }

        @Test
        @DisplayName("증가")
        void increaseQuantity() throws Exception {
            paramValue = "INCREASE";
            mockMvc.perform(patch(url + pathValue).session(mockHttpSession).param(paramName, paramValue))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andDo(print());

            ItemDto item = (ItemDto)redisCartTemplate.opsForHash().get(userId, "2_7_8");
            Assertions.assertEquals(item.getQuantity(), 2);
        }


        @Test
        @DisplayName("감소 2->1로")
        void decreaseCase1() throws Exception {
            paramValue = "INCREASE";
            mockMvc.perform(patch(url + pathValue).session(mockHttpSession).param(paramName, paramValue))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andDo(print());

            paramValue = "DECREASE";
            mockMvc.perform(patch(url + pathValue).session(mockHttpSession).param(paramName, paramValue))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andDo(print());

            ItemDto item = (ItemDto) redisCartTemplate.opsForHash().get(userId, "2_7_8");
            Assertions.assertEquals(item.getQuantity(), 1);
        }

        @Test
        @DisplayName("감소 1에서 진행하는경우 - 1이되어야한다")
        void decreaseCase2() throws Exception {
            paramValue = "DECREASE";
            mockMvc.perform(patch(url + pathValue).session(mockHttpSession).param(paramName, paramValue))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andDo(print());

            ItemDto item = (ItemDto)redisCartTemplate.opsForHash().get(userId, "2_7_8");
            Assertions.assertEquals(item.getQuantity(), 1);
        }


    }


    @Nested
    @DisplayName("GET : /carts")
    class GetItemList {
        private String url = "/carts";
        private String userId = "user1";

        private Long menuId = 2L;
        private String storeId = "1";

        @BeforeEach
        void setUp() {
            mockHttpSession.setAttribute(SessionConstants.SESSION_ID, userId);
            mockHttpSession.setAttribute(SessionConstants.AUTH_TYPE, UserType.USER);
        }

        @AfterEach
        void afterEach() {
            redisCartTemplate.delete("user1");
            redisCartTemplate.delete("STORE_ID");
        }

        @Test
        @DisplayName("성공")
        void getItemList() throws Exception {
            List<OptionDto> optionDtoList = optionMapper.findAllByMenuId(menuId);
            MenuDto menuDto = menuMapper.findById(menuId).get();
            ItemDto itemDto = new ItemDto(menuDto, optionDtoList);
            String json = objectMapper.writeValueAsString(itemDto);

            mockMvc.perform(post("/carts").session(mockHttpSession).param("storeId", storeId)
                            .content(json).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                    .andDo(print());


            mockMvc.perform(get(url).session(mockHttpSession))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.message").value(MessageConstants.SUCCESS_MESSAGE))
                    .andExpect(jsonPath("$.data.storeId").value(IsNull.notNullValue()))
                    .andDo(print());
        }

        @Test
        @DisplayName("장바구니가 비어있는경우")
        void cartEmpty() throws Exception {
            mockMvc.perform(get(url).session(mockHttpSession))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.data.storeId").value(IsNull.nullValue()))
                    .andDo(print());
        }
    }


}