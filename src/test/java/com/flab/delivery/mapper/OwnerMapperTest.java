package com.flab.delivery.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

@MybatisTest
class OwnerMapperTest extends AbstractSignUpTest {

    @Autowired
    OwnerMapper ownerMapper;

    @BeforeEach
    void before() {
        setMapper(ownerMapper);
    }
}