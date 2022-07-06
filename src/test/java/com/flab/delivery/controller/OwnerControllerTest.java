package com.flab.delivery.controller;

import com.flab.delivery.mapper.OwnerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

class OwnerControllerTest extends AbstractSignUpTest {

    public static final String OWNERS = "/owners";

    @Autowired
    OwnerMapper ownerMapper;


    @BeforeEach
    void before() {
        setMapper(ownerMapper);
        setUri(OWNERS);
    }

}