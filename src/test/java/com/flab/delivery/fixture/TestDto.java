package com.flab.delivery.fixture;

import com.flab.delivery.dto.address.AddressRequestDto;
import com.flab.delivery.dto.menu.MenuDto;
import com.flab.delivery.dto.option.OptionDto;
import com.flab.delivery.dto.order.*;
import com.flab.delivery.dto.user.PasswordDto;
import com.flab.delivery.dto.user.UserDto;
import com.flab.delivery.dto.user.UserInfoUpdateDto;
import com.flab.delivery.enums.OrderStatus;
import com.flab.delivery.enums.PayType;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.utils.PasswordEncoder;
import org.springframework.mock.web.MockHttpSession;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.flab.delivery.utils.SessionConstants.AUTH_TYPE;
import static com.flab.delivery.utils.SessionConstants.SESSION_ID;

public class TestDto {

    public static UserDto getUserDto() {
        return UserDto.builder()
                .id("user1")
                .password(PasswordEncoder.encrypt("1111"))
                .type(UserType.USER)
                .email("user1@email.com")
                .phoneNumber("010-1111-1111")
                .name("유저1")
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }

    public static UserInfoUpdateDto getUserInfoUpdateDto() {
        return UserInfoUpdateDto.builder()
                .id("user1")
                .name("테스트2")
                .phoneNumber("010-1234-1234")
                .email("test@naver.com")
                .build();
    }

    public static PasswordDto getPasswordDto() {
        return PasswordDto.builder()
                .password("1111")
                .newPassword("!NewPassword1234")
                .build();
    }

    public static AddressRequestDto getAddressRequestDto() {
        return AddressRequestDto.builder()
                .townName("운암동")
                .detailAddress("13번길 15")
                .alias("기타")
                .build();
    }

    public static OrderDto getOrderDto() {
        return OrderDto.builder()
                .totalPrice(30000)
                .orderStatus(OrderStatus.ORDER_REQUEST)
                .orderHistoryDto(OrderHistoryDto.from(getOrderRequestDto()))
                .deliveryAddress("운암동 13번길 15")
                .build();
    }

    private static List<OptionDto> getOptionList() {
        List<OptionDto> list = new ArrayList<>();

        for (int i = 0; i < 5; i++) {

            list.add(OptionDto.builder()
                    .menuId((long) i)
                    .price(100 * i)
                    .name("무추가" + i)
                    .build());
        }
        return list;
    }

    private static List<OrderMenuDto> getMenuList() {
        List<OrderMenuDto> list = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            list.add(OrderMenuDto.builder()
                    .menuDto(MenuDto.builder()
                            .id((long) i)
                            .name("치킨" + i)
                            .price(10000 * i)
                            .build())
                    .quantity(i + 1)
                    .optionList(getOptionList())
                    .build());
        }

        return list;
    }

    public static MockHttpSession createSessionBy(String userId, UserType userType) {
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute(SESSION_ID, userId);
        mockHttpSession.setAttribute(AUTH_TYPE, userType);
        return mockHttpSession;
    }

    public static OrderRequestDto getOrderRequestDto() {
        return OrderRequestDto
                .builder()
                .payType(PayType.CARD)
                .storeId(1L)
                .menuList(getMenuList())
                .build();
    }

    public static OrderSimpleResponseDto getOrderSimpleResponseDto(String menuName, int orderPrice) {
        return OrderSimpleResponseDto.builder()
                .storeId(1L)
                .orderPrice(orderPrice)
                .createdAt(LocalDateTime.now())
                .menuName(menuName)
                .menuCount(1)
                .status(OrderStatus.ORDER_APPROVED)
                .build();

    }
}
