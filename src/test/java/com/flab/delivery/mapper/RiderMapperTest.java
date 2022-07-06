package com.flab.delivery.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

class RiderMapperTest extends AbstractSignUpTest {

    @Autowired
    RiderMapper riderMapper;

    @BeforeEach
    void before() {
        setMapper(riderMapper);
    }
}