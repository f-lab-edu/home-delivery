package com.flab.delivery.controller.validator;

import com.flab.delivery.dto.user.PasswordDto;
import com.flab.delivery.dto.user.UserDto;
import com.flab.delivery.exception.SessionLoginException;
import com.flab.delivery.mapper.UserMapper;
import com.flab.delivery.utils.PasswordEncoder;
import com.flab.delivery.utils.SessionConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.servlet.http.HttpSession;

import static com.flab.delivery.exception.message.ErrorMessageConstants.UNAUTHORIZED_MESSAGE;

@Component
@RequiredArgsConstructor
public class PasswordValidator implements Validator {

    private final HttpSession httpSession;
    private final UserMapper userMapper;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(PasswordDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PasswordDto passwordDto = (PasswordDto) target;

        String userId = (String) httpSession.getAttribute(SessionConstants.SESSION_ID);

        if (userId == null) {
            throw new SessionLoginException(UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED);
        }

        UserDto findUser = userMapper.findById(userId);

        if (!PasswordEncoder.matches(passwordDto.getPassword(), findUser.getPassword())) {
            errors.rejectValue("newPassword", "worng.value", "입력하신 패스워드가 일치하지 않습니다.");
        }
    }
}
