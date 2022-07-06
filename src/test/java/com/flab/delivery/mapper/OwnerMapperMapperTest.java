package com.flab.delivery.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

class OwnerMapperMapperTest extends AbstractSignUpMapperTest {

    @Autowired
    OwnerMapper ownerMapper;

    @BeforeEach
    void before() {
        setMapper(ownerMapper);
    }
}