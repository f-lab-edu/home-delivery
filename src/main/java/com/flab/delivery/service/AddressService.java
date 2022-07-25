package com.flab.delivery.service;

import com.flab.delivery.dto.address.AddressDto;
import com.flab.delivery.dto.address.AddressRequestDto;

import java.util.List;

public interface AddressService {
    void addAddress(AddressRequestDto addressRequestDto, String userId);

    List<AddressDto> getAllAddress(String userId);
}
