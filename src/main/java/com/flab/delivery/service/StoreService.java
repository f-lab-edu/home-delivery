package com.flab.delivery.service;

import com.flab.delivery.dto.store.StoreDto;
import com.flab.delivery.dto.store.StoreRequestDto;
import com.flab.delivery.enums.StoreStatus;

import java.util.List;

public interface StoreService {

    void createStore(StoreRequestDto storeRequestDto, String userId);

    List<StoreDto> getOwnerStoreList(String userId);

    StoreDto getStore(Long storeId);

    void updateStore(Long storeId, StoreRequestDto storeRequestDto);

    void deleteStore(Long storeId);

    void changeStatus(Long storeId, StoreStatus status);


}
