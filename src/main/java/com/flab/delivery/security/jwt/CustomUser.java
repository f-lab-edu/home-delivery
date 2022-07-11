package com.flab.delivery.security.jwt;

import com.flab.delivery.dto.UserDto.LoginUserDto;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Getter
public class CustomUser extends User {
    private LoginUserDto userDto;

    /**
     * 불변과 메모리 절약을 위해 Collections.singleton 사용
     * Arrays.asList : 수정자 메서드로 값변경이 가능하며, Collection.singleton 보다 메모리를 더 차지함
     */
    public CustomUser(LoginUserDto userDto) {
        super(userDto.getId(), "", Collections.singleton(new SimpleGrantedAuthority(userDto.getLevel())));
        this.userDto = userDto;
    }
}
