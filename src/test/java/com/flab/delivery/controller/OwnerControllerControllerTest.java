package com.flab.delivery.controller;

import com.flab.delivery.mapper.OwnerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

class OwnerControllerControllerTest extends AbstractCommonControllerTest {

    public static final String OWNERS = "/owners";

    @Autowired
    OwnerMapper ownerMapper;


    @BeforeEach
    void before() {
        setMapper(ownerMapper);
        setUri(OWNERS);
    }

}