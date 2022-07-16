package com.flab.delivery.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HasAuthorization {

    UserType level() default UserType.ALL;

    enum UserType {
        ALL,
        USER,
        RIDER,
        OWNER
    }
}
