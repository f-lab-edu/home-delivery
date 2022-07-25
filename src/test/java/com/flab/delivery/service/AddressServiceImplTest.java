package com.flab.delivery.service;

import com.flab.delivery.dto.address.AddressRequestDto;
import com.flab.delivery.exception.AddressException;
import com.flab.delivery.fixture.TestDto;
import com.flab.delivery.mapper.AddressMapper;
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
class AddressServiceImplTest {

    private static final String SESSION_USER_ID = "user1";
    @InjectMocks
    private AddressServiceImpl addressService;

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
        verify(addressMapper, never()).addUserAddress(eq(requestDto), anyLong(), eq(SESSION_USER_ID));

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
        verify(addressMapper).addUserAddress(eq(requestDto), eq(1L), eq(SESSION_USER_ID));

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
        verify(addressMapper).addUserAddress(valueCapture.capture(), eq(1L), eq(SESSION_USER_ID));
        assertThat(valueCapture.getValue().getAlias()).isEqualTo(requestDto.getDetailAddress());

    }
}