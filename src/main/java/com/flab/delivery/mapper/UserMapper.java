package com.flab.delivery.mapper;

import com.flab.delivery.dto.user.PasswordDto;
import com.flab.delivery.dto.user.SignUpDto;
import com.flab.delivery.dto.user.UserDto;
import com.flab.delivery.dto.user.UserInfoUpdateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    void save(SignUpDto userDto);

    boolean idExists(String id);


    UserDto findById(String id);

    int updateInfo(UserInfoUpdateDto userInfoUpdateDto);

    void deleteById(String userId);

    void changePassword(@Param("userId") String userId, @Param("newPassword") String newPassword);
}
