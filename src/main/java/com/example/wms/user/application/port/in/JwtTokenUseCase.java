package com.example.wms.user.application.port.in;

import com.example.wms.user.adapter.in.dto.response.TokenInfo;
import org.springframework.security.core.Authentication;

public interface JwtTokenUseCase {
    TokenInfo generateAndSaveTokens(String staffNumber, String password);
    String getStaffNumberFromToken(String token);
    void invalidateAccessToken(String token);
    long getRemainingExpiration(String token);
}
