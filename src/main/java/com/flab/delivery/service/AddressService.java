package com.flab.delivery.service;

import com.flab.delivery.dto.address.AddressRequestDto;

public interface AddressService {
    void addAddress(AddressRequestDto addressRequestDto, String userId);
}
