package com.flab.delivery.mapper;

import com.flab.delivery.config.DatabaseConfig;
import com.flab.delivery.dto.address.AddressRequestDto;
import com.flab.delivery.fixture.TestDto;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@MybatisTest
@Import({DatabaseConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AddressMapperTest {

    @Autowired
    private AddressMapper addressMapper;

    @Test
    void findIdByTownName_존재하는_주소_성공() {
        // given
        String townName = "운암동";

        // when
        Long byTownName = addressMapper.findIdByTownName(townName);

        //then
        assertThat(byTownName).isEqualTo(1L);
    }

    @Test
    void findIdByTownName_존재하지_않는_주소_실패() {
        // given
        String townName = "운암동2";

        // when
        Long byTownName = addressMapper.findIdByTownName(townName);

        //then
        assertThat(byTownName).isNull();
    }

    @Test
    void addAddress_성공() {
        // given
        AddressRequestDto requestDto = TestDto.getAddressRequestDto();

        Long addressId = addressMapper.findIdByTownName(requestDto.getTownName());
        String userId = "user1";

        // when
        Long result = addressMapper.addUserAddress(requestDto, addressId, userId);

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
        assertThatThrownBy(() -> addressMapper.addUserAddress(requestDto, addressId, userId))
                .isInstanceOf(DataAccessException.class);
    }

    @Test
    void addAddress_유저_아이디_없어서_실패() {
        // given
        AddressRequestDto requestDto = TestDto.getAddressRequestDto();

        Long addressId = addressMapper.findIdByTownName(requestDto.getTownName());

        // when
        //then
        assertThatThrownBy(() -> addressMapper.addUserAddress(requestDto, addressId, null))
                .isInstanceOf(DataAccessException.class);
    }
}