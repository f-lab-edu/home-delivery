package com.flab.delivery.controller;


import com.flab.delivery.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;


class UserControllerTest extends AbstractSignUpTest {

    public static final String USERS = "/users";

    @Autowired
    UserMapper userMapper;

    @BeforeEach
    void before() {
        setMapper(userMapper);
        setUri(USERS);
    }

}