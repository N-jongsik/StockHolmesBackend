package com.example.wms.user.application.port.out;

import com.example.wms.user.application.domain.LogoutAccessToken;

import java.util.Optional;

public interface LogoutAccessTokenPort {
    void save(LogoutAccessToken logoutAccessToken);  // 로그아웃한 토큰 저장
    Optional<LogoutAccessToken> findByStaffNumber(String staffNumber);  // 사번으로 리프레시 토큰 조회
    void deleteByStaffNumber(String staffNumber);  // 사번으로 리프레시 토큰 삭제
}
