package com.flab.delivery.service;

import com.flab.delivery.dto.option.OptionRequestDto;
import com.flab.delivery.exception.OptionException;
import com.flab.delivery.mapper.OptionMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OptionServiceTest {

    @Mock
    OptionMapper optionMapper;

    @InjectMocks
    OptionService optionService;

    @Nested
    @DisplayName("옵션 생성")
    class CreateOption {

        private Long menuId = 1L;
        private String optionName = "마늘소스추가";

        private OptionRequestDto getRequestDto() {
            return OptionRequestDto.builder().menuId(menuId).name(optionName).build();
        }


        @Test
        @DisplayName("성공")
        void success() {
            // given
            when(optionMapper.existsByName(menuId, optionName)).thenReturn(Optional.empty());
            // when
            optionService.createOption(getRequestDto());
            when(optionMapper.existsByName(menuId, optionName)).thenReturn(Optional.of(menuId));
            OptionException ex = Assertions.assertThrows(OptionException.class, () -> {
                optionService.createOption(getRequestDto());
            });
            // then
            Assertions.assertEquals(ex.getMessage(), "이미 존재하는 옵션입니다");

        }

        @Test
        @DisplayName("실패 - 이미존재하는경우")
        void fail() {
            // given
            // when
            when(optionMapper.existsByName(menuId, optionName)).thenReturn(Optional.of(menuId));
            OptionException ex = Assertions.assertThrows(OptionException.class, () -> {
                optionService.createOption(getRequestDto());
            });
            // then
            Assertions.assertEquals(ex.getMessage(), "이미 존재하는 옵션입니다");
        }
    }

    @Nested
    @DisplayName("옵션 정보 변경")
    class UpdateOption {

        private Long id = 1L;
        private Long menuId = 1L;
        private String name = "치즈볼 추가";

        private OptionRequestDto getRequestDto() {
            return OptionRequestDto.builder()
                    .menuId(menuId)
                    .name(name)
                    .build();
        }


        @Test
        @DisplayName("성공")
        void success() {
            // given
            String changeName = "초코볼 추가";
            OptionRequestDto requestDto = getRequestDto();
            name = changeName;
            optionService.updateOption(id, requestDto);
            verify(optionMapper, times(1)).updateById(id, requestDto);
        }

    }


    @Nested
    @DisplayName("옵션삭제")
    class DeleteOption {
        private Long id = 1L;

        @Test
        @DisplayName("성공")
        void success() {
            optionService.deleteOption(id);
            verify(optionMapper, times(1)).deleteById(id);
        }

    }

    @Nested
    @DisplayName("옵션 리스트 가져오기")
    class GetOptionList {
        private Long menuId = 1L;

        @Test
        @DisplayName("성공")
        void success() {
            optionService.getOptionList(menuId);
            verify(optionMapper, times(1)).findAllByMenuId(menuId);
        }
    }

}