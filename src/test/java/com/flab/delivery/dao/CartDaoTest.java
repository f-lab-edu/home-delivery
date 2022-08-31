package com.flab.delivery.dao;


import com.flab.delivery.dto.cart.CartResponseDto;
import com.flab.delivery.dto.cart.ItemDto;
import com.flab.delivery.dto.menu.MenuDto;
import com.flab.delivery.dto.option.OptionDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = CartDaoTest.ContainerPropertyInitializer.class)
public class CartDaoTest {

    @Autowired
    CartDao cartDao;

    @Autowired
    RedisTemplate<String, Object> redisCartTemplate;

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

    String userId = "user1";
    String storeId = "1";

    @AfterEach
    void afterEach() {
        redisCartTemplate.delete(userId);
        redisCartTemplate.delete("STORE_ID");
    }


    @Nested
    @DisplayName("장바구니에 담기")
    class InsertItem {

        MenuDto menuDto = MenuDto.builder().id(1L).menuGroupId(2L).name("후라이드치킨").build();
        List<OptionDto> optionDtoList = new ArrayList<>();
        OptionDto optionDto = OptionDto.builder().id(1L).name("콜라추가").build();
        ItemDto itemDto;

        private ItemDto getItemDto() {
            optionDtoList.add(optionDto);
            return new ItemDto(menuDto, optionDtoList);
        }

        @Test
        @DisplayName("처음 물품담기")
        void insertItem_처음() {
            itemDto = getItemDto();
            cartDao.insertItem(userId, storeId, itemDto);

            CartResponseDto allItem = cartDao.findAllItem(userId);
            Assertions.assertEquals(new Long(storeId), allItem.getStoreId());
        }

        @Test
        @DisplayName("이미존재하고 메뉴다른경우")
        void insertItem_메뉴다른경우() {
            itemDto = getItemDto();
            cartDao.insertItem(userId, storeId, itemDto);
            optionDtoList.add(OptionDto.builder().name("치즈볼추가").id(5L).build());
            ItemDto diffItemDto = getItemDto();
            cartDao.insertItem(userId, storeId, diffItemDto);
            CartResponseDto allItem = cartDao.findAllItem(userId);
            Assertions.assertEquals(2, allItem.getCartList().size());

        }

        @Test
        @DisplayName("이미존재하고 메뉴같은경우")
        void insertItem_메뉴같은경우() {
            itemDto = getItemDto();
            cartDao.insertItem(userId, storeId, itemDto);
            cartDao.insertItem(userId, storeId, itemDto);
            CartResponseDto allItem = cartDao.findAllItem(userId);
            Assertions.assertEquals(2, allItem.getCartList().get(0).getQuantity());
        }

        @Test
        @DisplayName("매장 다른경우")
        void insertItem_매장다른경우() {
            String diffStoreId = "2";
            itemDto = getItemDto();
            cartDao.insertItem(userId, storeId, itemDto);
            cartDao.insertItem(userId, diffStoreId, itemDto);

            CartResponseDto allItem = cartDao.findAllItem(userId);
            Assertions.assertEquals(new Long(diffStoreId), allItem.getStoreId());
        }
    }

    @Test
    @DisplayName("담은메뉴 삭제")
    void deleteItem() {
        MenuDto menuDto = MenuDto.builder().id(2L).build();
        List<OptionDto> list = new ArrayList<>();
        OptionDto optionDto1 = OptionDto.builder().id(7L).build();
        OptionDto optionDto2 = OptionDto.builder().id(8L).build();
        list.add(optionDto1);
        list.add(optionDto2);

        ItemDto itemDto = new ItemDto(menuDto, list);
        cartDao.insertItem(userId, "1", itemDto);
        cartDao.deleteItem(userId, "2_7_8");
        CartResponseDto allItem = cartDao.findAllItem(userId);
        Assertions.assertNull(allItem.getStoreId());
    }

    @Test
    @DisplayName("장바구니 목록조회")
    void findAllItem() {
        MenuDto menuDto = MenuDto.builder().id(2L).build();
        List<OptionDto> list = new ArrayList<>();
        OptionDto optionDto1 = OptionDto.builder().id(7L).build();
        OptionDto optionDto2 = OptionDto.builder().id(8L).build();
        list.add(optionDto1);
        list.add(optionDto2);

        cartDao.insertItem(userId, "1", new ItemDto(menuDto, list));

        CartResponseDto allItem = cartDao.findAllItem(userId);
        Assertions.assertEquals(1, allItem.getCartList().size());
    }

    @Nested
    @DisplayName("수량변경")
    class UpdateQuantity {
        MenuDto menuDto = MenuDto.builder().id(2L).build();
        List<OptionDto> list = new ArrayList<>();
        OptionDto optionDto = OptionDto.builder().id(7L).build();

        String cartKey = "2_7";

        @AfterEach
        void afterEach() {
            cartDao.deleteItem(userId, cartKey);
        }

        @Test
        @DisplayName("수량증가")
        void increaseQuantity() {
            list.add(optionDto);
            cartDao.insertItem(userId, "1", new ItemDto(menuDto, list));
            cartDao.increaseQuantity(userId, cartKey);
            CartResponseDto allItem = cartDao.findAllItem(userId);
            Assertions.assertEquals(2, allItem.getCartList().get(0).getQuantity());
        }


        @Test
        @DisplayName("수량감소")
        void decreaseQuantity() {
            list.add(optionDto);
            cartDao.insertItem(userId, "1", new ItemDto(menuDto, list));
            cartDao.increaseQuantity(userId, cartKey);
            cartDao.increaseQuantity(userId, cartKey);
            cartDao.decreaseQuantity(userId, cartKey);
            CartResponseDto allItem = cartDao.findAllItem(userId);
            Assertions.assertEquals(2, allItem.getCartList().get(0).getQuantity());
        }
    }


}
