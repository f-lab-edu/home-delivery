package com.flab.delivery.service;

import com.flab.delivery.dto.option.OptionDto;
import com.flab.delivery.dto.option.OptionRequestDto;
import com.flab.delivery.exception.OptionException;
import com.flab.delivery.mapper.OptionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OptionService {

    private final OptionMapper optionMapper;

    public void createOption(OptionRequestDto requestDto) {
        if (optionMapper.existsByName(requestDto.getMenuId(), requestDto.getName()).isPresent()) {
            throw new OptionException("이미 존재하는 옵션입니다", HttpStatus.BAD_REQUEST);
        }

        optionMapper.save(requestDto);
    }

    public void updateOption(Long id, OptionRequestDto requestDto) {
        optionMapper.updateById(id, requestDto);
    }

    public void deleteOption(Long id) {
        optionMapper.deleteById(id);
    }

    public List<OptionDto> getOptionList(Long menuId) {
        return optionMapper.findAllByMenuId(menuId);
    }

}
