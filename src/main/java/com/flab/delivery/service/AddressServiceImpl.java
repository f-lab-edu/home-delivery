package com.flab.delivery.service;

import com.flab.delivery.dto.address.AddressRequestDto;
import com.flab.delivery.exception.AddressException;
import com.flab.delivery.mapper.AddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressMapper addressMapper;

    @Override
    public void addAddress(AddressRequestDto addressRequestDto, String userId) {

        Long findAddressId = addressMapper.findIdByTownName(addressRequestDto.getTownName());

        if (findAddressId == null) {
            throw new AddressException("존재하지 않는 주소 입니다.");
        }

        if (addressRequestDto.getAlias() == null) {
            addressRequestDto.changeAlias(addressRequestDto.getDetailAddress());
        }

        addressMapper.addUserAddress(addressRequestDto, findAddressId, userId);

    }
}
