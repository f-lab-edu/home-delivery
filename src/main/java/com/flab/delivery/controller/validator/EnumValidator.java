package com.flab.delivery.controller.validator;

import com.flab.delivery.annotation.ValidEnum;

import java.util.Arrays;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum> {
    Enum<?>[] enumConstants;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        enumConstants = constraintAnnotation.enumClass().getEnumConstants();
    }

    @Override
    public boolean isValid(Enum value, ConstraintValidatorContext context) {
        return Arrays.stream(enumConstants).anyMatch(anEnum -> anEnum == value);
    }
}