package com.flab.delivery.service;

import com.flab.delivery.dto.store.StoreDto;
import com.flab.delivery.dto.store.StoreRequestDto;
import com.flab.delivery.enums.StoreStatus;
import com.flab.delivery.exception.NotFoundException;
import com.flab.delivery.exception.StoreException;
import com.flab.delivery.mapper.StoreMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreMapper storeMapper;

    @Override
    public void createStore(StoreRequestDto storeRequestDto, String userId) {
        // 매장 이름으로 중복 체크를 할것입니다 - 이름 + 상세주소
        Optional<Long> existsStore = storeMapper.existsByNameAndDetailAddress(storeRequestDto.getName(),
                storeRequestDto.getDetailAddress());
        if (existsStore.isPresent()) {
            throw new StoreException("이미 존재하는 매장입니다");
        }
        storeMapper.save(storeRequestDto, userId);
    }


    @Override
    public List<StoreDto> getOwnerStoreList(String userId) {
        return storeMapper.findAllByUserId(userId);
    }

    @Override
    public StoreDto getStore(Long id) {
        return findStore(id);
    }

    @Override
    public void updateStore(Long id, StoreRequestDto storeRequestDto) {
        findStore(id);
        storeMapper.updateById(id, storeRequestDto);
    }

    @Override
    public void deleteStore(Long id) {
        findStore(id);
        storeMapper.deleteById(id);
    }

    @Override
    public void changeStatus(Long id, StoreStatus status) {
        findStore(id);
        storeMapper.updateStatusById(id, status);
    }

    private StoreDto findStore(Long storeId) {
        return storeMapper.findById(storeId).orElseThrow(
                () -> new NotFoundException("존재하지 않는 매장입니다")
        );
    }


}
