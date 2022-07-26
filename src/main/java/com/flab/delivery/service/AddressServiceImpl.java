package com.flab.delivery.service;

import com.flab.delivery.dto.address.AddressDto;
import com.flab.delivery.dto.address.AddressRequestDto;
import com.flab.delivery.exception.AddressException;
import com.flab.delivery.mapper.AddressMapper;
import com.flab.delivery.mapper.UserAddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressMapper addressMapper;
    private final UserAddressMapper userAddressMapper;

    @Override
    public void addAddress(AddressRequestDto addressRequestDto, String userId) {

        Long findAddressId = addressMapper.findIdByTownName(addressRequestDto.getTownName());

        if (findAddressId == null) {
            throw new AddressException("존재하지 않는 주소 입니다.");
        }

        if (addressRequestDto.getAlias() == null) {
            addressRequestDto.changeAlias(addressRequestDto.getDetailAddress());
        }

        userAddressMapper.addAddress(addressRequestDto, findAddressId, userId);

    }

    @Override
    public List<AddressDto> getAllAddress(String userId) {

        return userAddressMapper.findAllByUserId(userId);
    }

    @Override
    public void removeAddress(Long userAddressId, String userId) {

        if (!userAddressMapper.existsById(userAddressId)) {
            throw new AddressException("존재하지 않는 주소 입니다.");
        }

        userAddressMapper.deleteById(userAddressId);
    }
}
