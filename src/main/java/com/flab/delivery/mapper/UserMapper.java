package com.flab.delivery.mapper;

import com.flab.delivery.dto.user.SignUpDto;
import com.flab.delivery.dto.user.UserDto;
import com.flab.delivery.dto.user.UserInfoUpdateDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    void save(SignUpDto userDto);

    boolean idExists(String id);


    UserDto findById(String id);

    int updateInfo(UserInfoUpdateDto userInfoUpdateDto);

    void deleteById(String userId);
}
