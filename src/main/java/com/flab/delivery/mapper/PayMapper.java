package com.flab.delivery.mapper;

import com.flab.delivery.dto.pay.PayDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayMapper {
    void save(PayDto payDto);
}
