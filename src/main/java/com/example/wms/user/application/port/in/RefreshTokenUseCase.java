package com.example.wms.user.application.port.in;

import com.example.wms.user.adapter.in.dto.response.TokenInfo;
import com.example.wms.user.application.domain.RefreshToken;

public interface RefreshTokenUseCase {

    void saveRefreshToken(String email, String refreshToken);
    void deleteByStaffNumber(String email);
    void validateRefreshToken(String refreshToken, String email);
    RefreshToken findRefreshToken(String email);
    String validateAndGetStaffNumber(String refreshToken);
    void checkRefreshToken(String refreshToken, String email);
}
