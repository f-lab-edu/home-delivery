package com.flab.delivery.service;

import com.flab.delivery.dto.address.AddressRequestDto;
import com.flab.delivery.exception.AddressException;
import com.flab.delivery.fixture.TestDto;
import com.flab.delivery.mapper.AddressMapper;
import com.flab.delivery.mapper.UserAddressMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    private static final String SESSION_USER_ID = "user1";
    @InjectMocks
    private AddressService addressService;

    @Mock
    private UserAddressMapper userAddressMapper;

    @Mock
    private AddressMapper addressMapper;


    @Test
    void addAddress_존재하지_않는_주소_실패() {
        // given
        AddressRequestDto requestDto = TestDto.getAddressRequestDto();
        given(addressMapper.findIdByTownName(requestDto.getTownName())).willReturn(null);

        // when
        //then
        assertThatThrownBy(() -> addressService.addAddress(requestDto, SESSION_USER_ID))
                .isInstanceOf(AddressException.class);

        verify(addressMapper).findIdByTownName(anyString());
        verify(userAddressMapper, never()).addAddress(eq(requestDto), anyLong(), eq(SESSION_USER_ID));

    }

    @Test
    void addAddress_성공() {
        // given
        AddressRequestDto requestDto = TestDto.getAddressRequestDto();
        given(addressMapper.findIdByTownName(requestDto.getTownName())).willReturn(1L);

        // when
        addressService.addAddress(requestDto, SESSION_USER_ID);

        //then
        verify(addressMapper).findIdByTownName(anyString());
        verify(userAddressMapper).addAddress(eq(requestDto), eq(1L), eq(SESSION_USER_ID));

    }

    @Test
    void addAddress_별칭_없을시_상세주소로_변경_성공() {
        // given
        AddressRequestDto requestDto = AddressRequestDto.builder()
                .townName("운암동")
                .detailAddress("15번길 13로")
                .build();
        given(addressMapper.findIdByTownName(requestDto.getTownName())).willReturn(1L);
        ArgumentCaptor<AddressRequestDto> valueCapture = ArgumentCaptor.forClass(AddressRequestDto.class);

        // when
        addressService.addAddress(requestDto, SESSION_USER_ID);

        //then
        verify(addressMapper).findIdByTownName(anyString());
        verify(userAddressMapper).addAddress(valueCapture.capture(), eq(1L), eq(SESSION_USER_ID));
        assertThat(valueCapture.getValue().getAlias()).isEqualTo(requestDto.getDetailAddress());

    }


    @Test
    void removeAddress_성공() {
        // given
        Long id = 1L;
        given(userAddressMapper.existsById(id)).willReturn(true);

        // when
        addressService.removeAddress(id, SESSION_USER_ID);

        // then
        verify(userAddressMapper).existsById(id);
        verify(userAddressMapper).deleteById(eq(id), eq(SESSION_USER_ID));
    }

    @Test
    void removeAddress_존재하지_않는_아이디_실패() {
        // given
        Long id = 1L;
        given(userAddressMapper.existsById(id)).willReturn(false);

        // when
        assertThatThrownBy(() -> addressService.removeAddress(id, SESSION_USER_ID))
                .isInstanceOf(AddressException.class);

        // then
        verify(userAddressMapper).existsById(id);
        verify(userAddressMapper, never()).deleteById(eq(id), eq(SESSION_USER_ID));
    }

    @Test
    void selectAddress_성공() {
        // given
        Long id = 1L;
        given(userAddressMapper.existsById(eq(id))).willReturn(true);
        given(userAddressMapper.changeAddress(eq(id), eq(SESSION_USER_ID))).willReturn(1);

        // when
        addressService.selectAddress(id, SESSION_USER_ID);

        // then
        verify(userAddressMapper).changeAddress(eq(id), eq(SESSION_USER_ID));
        verify(userAddressMapper).resetSelection(eq(SESSION_USER_ID));
        verify(userAddressMapper).existsById(eq(id));
    }

    @Test
    void selectAddress_존재하지_않는_주소_실패() {
        // given
        Long id = 1L;
        given(userAddressMapper.existsById(eq(id))).willReturn(false);

        // when
        assertThatThrownBy(() -> addressService.selectAddress(id, SESSION_USER_ID))
                .isInstanceOf(AddressException.class);
        // then
        verify(userAddressMapper, never()).changeAddress(eq(id), eq(SESSION_USER_ID));
        verify(userAddressMapper, never()).resetSelection(eq(SESSION_USER_ID));
        verify(userAddressMapper).existsById(eq(id));
    }

    @Test
    void selectAddress_잘못된_요청_실패() {
        // given
        Long id = 1L;
        given(userAddressMapper.existsById(eq(id))).willReturn(true);
        given(userAddressMapper.changeAddress(eq(id), eq(SESSION_USER_ID))).willReturn(0);

        // when
        assertThatThrownBy(() -> addressService.selectAddress(id, SESSION_USER_ID))
                .isInstanceOf(AddressException.class);
        // then
        verify(userAddressMapper).changeAddress(eq(id), eq(SESSION_USER_ID));
        verify(userAddressMapper).resetSelection(eq(SESSION_USER_ID));
        verify(userAddressMapper).existsById(eq(id));
    }
}