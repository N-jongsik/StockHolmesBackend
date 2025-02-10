package com.example.wms.user.application.port.out;

import com.example.wms.user.adapter.in.dto.response.UserInfoResDto;
import com.example.wms.user.application.domain.User;

public interface UserPort {
    void deleteUser(String staffNumber); //회원 탈퇴
    User findByStaffNumber(String staffNumber); //정보조회
}
