package com.flab.delivery.controller.validator;

import com.flab.delivery.dto.user.PasswordDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PasswordValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(PasswordDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PasswordDto passwordDto = (PasswordDto) target;

        if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmedNewPassword())) {
            errors.rejectValue("newPassword", "worng.value", "입력하신 패스워드가 일치하지 않습니다.");
        }
    }
}
