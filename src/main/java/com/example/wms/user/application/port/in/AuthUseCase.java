package com.example.wms.user.application.port.in;

import com.example.wms.user.adapter.in.dto.request.LoginReqDto;
import com.example.wms.user.adapter.in.dto.request.SignUpReqDto;
import com.example.wms.user.adapter.in.dto.response.AuthenticatedResDto;
import com.example.wms.user.adapter.in.dto.response.TokenInfo;


//service 인터페이스
public interface AuthUseCase {
    AuthenticatedResDto signUp(SignUpReqDto signUpReqDto); //등록
    AuthenticatedResDto login(LoginReqDto loginReqDto); //로그인
    void logout(String accessToken); //로그아웃
    TokenInfo reissueToken(String refreshToken);
    void updateUserRole(String staffNumber, String newRole);
    void updateActive(String staffNumber, boolean isActive);
    void updateUserPassword(String staffNumber, String newPassword);
}
