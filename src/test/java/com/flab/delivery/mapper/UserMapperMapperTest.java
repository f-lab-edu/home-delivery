package com.flab.delivery.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

class UserMapperMapperTest extends AbstractCommonMapperTest {

    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    void before() {
        setMapper(userMapper);
    }

}