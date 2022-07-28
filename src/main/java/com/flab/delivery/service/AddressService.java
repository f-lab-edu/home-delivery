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

    /**
     * 기본적으로 UnCheckedException 은 예외가 발생하더라도 롤백되지 않습니다.
     * 현재의 경우 잘못된 id 또는 userId 에 의해 changeAddress 로 영향을 받은 레코드가 없다면
     * AddressException 을 발생시켜 작업을 롤백시켜 기존 선택 주소를 유지합니다.
     */
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
