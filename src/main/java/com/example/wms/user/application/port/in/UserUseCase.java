package com.example.wms.user.application.port.in;

import com.example.wms.user.adapter.in.dto.response.UserInfoResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserUseCase {
    void deleteUser();
    UserInfoResDto findUser();
    UserInfoResDto findUserByStaffNumber(String staffNumber);
    Page<UserInfoResDto> findAllUsers(Pageable pageable);
}