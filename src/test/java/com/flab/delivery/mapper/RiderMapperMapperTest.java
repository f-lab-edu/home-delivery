package com.flab.delivery.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

class RiderMapperMapperTest extends AbstractCommonMapperTest {

    @Autowired
    RiderMapper riderMapper;

    @BeforeEach
    void before() {
        setMapper(riderMapper);
    }
}