package com.flab.delivery.annotation;

import com.flab.delivery.enums.UserType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LoginCheck {
    UserType userType() default UserType.ALL;
}
