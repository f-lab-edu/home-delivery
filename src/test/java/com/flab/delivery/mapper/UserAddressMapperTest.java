package com.flab.delivery.mapper;


import com.flab.delivery.config.DatabaseConfig;
import com.flab.delivery.dto.address.AddressDto;
import com.flab.delivery.dto.address.AddressRequestDto;
import com.flab.delivery.fixture.TestDto;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@MybatisTest
@Import({DatabaseConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserAddressMapperTest {

    @Autowired
    AddressMapper addressMapper;

    @Autowired
    UserAddressMapper userAddressMapper;

    @Test
    void addAddress_성공() {
        // given
        AddressRequestDto requestDto = TestDto.getAddressRequestDto();

        Long addressId = addressMapper.findIdByTownName(requestDto.getTownName());
        String userId = "user1";

        // when
        Long result = userAddressMapper.addAddress(requestDto, addressId, userId);

        //then
        assertThat(result).isNotNull();
    }

    @Test
    void addAddress_주소_아이디_없어서_실패() {
        // given
        AddressRequestDto requestDto = TestDto.getAddressRequestDto();

        Long addressId = null;
        String userId = "user1";

        // when
        //then
        assertThatThrownBy(() -> userAddressMapper.addAddress(requestDto, addressId, userId))
                .isInstanceOf(DataAccessException.class);
    }

    @Test
    void addAddress_유저_아이디_없어서_실패() {
        // given
        AddressRequestDto requestDto = TestDto.getAddressRequestDto();

        Long addressId = addressMapper.findIdByTownName(requestDto.getTownName());

        // when
        //then
        assertThatThrownBy(() -> userAddressMapper.addAddress(requestDto, addressId, null))
                .isInstanceOf(DataAccessException.class);
    }


    @Test
    void findAllByUserId_성공() {
        // given
        String userId = "user1";

        // when
        List<AddressDto> addressDtoList = userAddressMapper.findAllByUserId(userId);

        // then
        for (AddressDto addressDto : addressDtoList) {
            assertThat(addressDto.getId()).isNotNull();
            assertThat(addressDto.getDetailAddress()).isNotNull();
            assertThat(addressDto.getAlias()).isNotNull();
            assertThat(addressDto.getTownName()).isNotNull();
            assertThat(addressDto.isSelected()).isNotNull();
        }
    }

    @Test
    void findAllByUserId_잘못된_유저로_조회시_빈리스트_반환() {
        // given
        String userId = "wrongUser";

        // when
        List<AddressDto> addressDtoList = userAddressMapper.findAllByUserId(userId);

        // then
        assertThat(addressDtoList).isEmpty();

    }

    @Test
    void existsById_존재해서_True() {
        // given
        Long id = userAddressMapper.findAllByUserId("user1").get(0).getId();

        // when
        boolean existsById = userAddressMapper.existsById(id);

        // then
        assertThat(existsById).isTrue();
    }


    @Test
    void existsById_존재하지_않아서_False() {
        // given
        Long id = null;

        // when
        boolean existsById = userAddressMapper.existsById(id);

        // then
        assertThat(existsById).isFalse();
    }

    @Test
    void deleteById_존재하는_아이디_성공() {
        // given
        Long id = userAddressMapper.findAllByUserId("user1").get(0).getId();

        // when
        userAddressMapper.deleteById(id);

        // then
        assertThat(userAddressMapper.existsById(id)).isFalse();
    }
}
