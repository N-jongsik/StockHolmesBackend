package com.example.wms.user.application.port.in;

import com.example.wms.user.adapter.in.dto.response.UserInfoResDto;

import java.util.List;

public interface UserUseCase {

    void deleteUser();
    UserInfoResDto findUser();
    UserInfoResDto findUserByStaffNumber(String staffNumber);
    List<UserInfoResDto> findAllUsers(int pageSize, int offset);
}
