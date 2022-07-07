package com.flab.delivery.controller;

import com.flab.delivery.mapper.RiderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

class RiderControllerControllerTest extends AbstractCommonControllerTest {

    public static final String RIDER = "/riders";

    @Autowired
    RiderMapper riderMapper;


    @BeforeEach
    void before() {
        setMapper(riderMapper);
        setUri(RIDER);
    }

}