package com.flab.delivery.service;

import com.flab.delivery.dao.CartDao;
import com.flab.delivery.dto.cart.ItemDto;
import com.flab.delivery.dto.menu.MenuDto;
import com.flab.delivery.dto.option.OptionDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @InjectMocks
    CartService cartService;

    @Mock
    CartDao cartDao;

    @Nested
    @DisplayName("장바구니 담기")
    class InsertItem {

        private String userId = "user1";
        private String storeId = "1";

        private MenuDto getMenuDto() {
            return MenuDto.builder()
                    .id(2L)
                    .name("후라이드 치킨")
                    .build();
        }

        private List<OptionDto> getOptionDtoList() {
            List<OptionDto> optionDtoList = new ArrayList<>();
            optionDtoList.add(OptionDto.builder()
                    .id(7L)
                    .menuId(2L)
                    .name("콜라추가")
                    .build());
            return optionDtoList;
        }


        private ItemDto getItemDto() {
            return new ItemDto(getMenuDto(), getOptionDtoList());
        }

        @Test
        @DisplayName("담기 성공")
        void success() {
            // given
            ItemDto itemDto = getItemDto();
            // when
            cartService.insertItem(userId, storeId, itemDto);
            // then
            verify(cartDao, times(1)).insertItem(userId, storeId, itemDto);
        }


    }


    @Test
    @DisplayName("성공")
    void deleteItem() {
        String userId = "user1";
        String cartKey = "2_7_8";
        cartService.deleteItem(userId, cartKey);
        verify(cartDao, times(1)).deleteItem(userId, cartKey);


    }

    @Test
    @DisplayName("수량 증가")
    void increaseQuantity() {
        String userId = "user1";
        String cartKey = "2_7_8";
        cartService.increaseQuantity(userId, cartKey);
        verify(cartDao, times(1)).increaseQuantity(userId, cartKey);
    }

    @Test
    @DisplayName("수량 감소")
    void decreaseQuantity() {
        String userId = "user1";
        String cartKey = "2_7_8";
        cartService.decreaseQuantity(userId, cartKey);
        verify(cartDao, times(1)).decreaseQuantity(userId, cartKey);
    }

    @Test
    @DisplayName("장바구니 조회")
    void findAllItem() {
        String userId = "user1";
        cartService.findAllItem(userId);
        verify(cartDao, times(1)).findAllItem(userId);
    }

}