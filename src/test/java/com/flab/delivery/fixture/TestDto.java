package com.flab.delivery.fixture;

import com.flab.delivery.dto.address.AddressRequestDto;
import com.flab.delivery.dto.order.OrderDto;
import com.flab.delivery.dto.order.OrderHistoryDto;
import com.flab.delivery.dto.user.PasswordDto;
import com.flab.delivery.dto.user.UserDto;
import com.flab.delivery.dto.user.UserInfoUpdateDto;
import com.flab.delivery.enums.OrderStatus;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.utils.PasswordEncoder;
import org.springframework.mock.web.MockHttpSession;

import java.time.LocalDateTime;
import java.util.Arrays;
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
                .deliveryPrice(3000)
                .orderHistoryList(Arrays.asList(
                        OrderHistoryDto.builder().menuName("치킨").price(10000).quantity(1).build(),
                        OrderHistoryDto.builder().menuName("피자").price(20000).quantity(2).build()
                )).build();
    }

    public static MockHttpSession createSessionBy(String userId, UserType userType) {
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute(SESSION_ID, userId);
        mockHttpSession.setAttribute(AUTH_TYPE, userType);
        return mockHttpSession;
    }
}
