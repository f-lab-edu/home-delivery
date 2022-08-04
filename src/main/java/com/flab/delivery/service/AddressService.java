package com.flab.delivery.service;

import com.flab.delivery.dto.address.AddressDto;
import com.flab.delivery.dto.address.AddressRequestDto;
import com.flab.delivery.exception.AddressException;
import com.flab.delivery.mapper.AddressMapper;
import com.flab.delivery.mapper.UserAddressMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressMapper addressMapper;
    private final UserAddressMapper userAddressMapper;

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

    public List<AddressDto> getAllAddress(String userId) {

        return userAddressMapper.findAllByUserId(userId);
    }

    public void removeAddress(Long id, String userId) {

        if (!isExistsById(id)) {
            throw new AddressException("존재하지 않는 주소 입니다.");
        }

        userAddressMapper.deleteById(id, userId);
    }


    @Transactional(rollbackFor = AddressException.class)
    public void selectAddress(Long id, String userId) {

        int updateCount = userAddressMapper.changeAddress(id, userId);

        if (updateCount == 0) {
            throw new AddressException("잘못된 요청 입니다.");
        }

        userAddressMapper.resetSelection(userId);
    }

    private boolean isExistsById(Long id) {
        return userAddressMapper.existsById(id);
    }
}
