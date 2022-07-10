package com.flab.delivery.security.session;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HasCertify {

    UserLevel level();

    enum UserLevel {
        ALL, USER, RIDER, OWNER
    }
}
