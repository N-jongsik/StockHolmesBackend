package com.example.wms.user.application.port.out;

import com.example.wms.user.adapter.in.dto.response.TokenInfo;
import org.springframework.security.core.Authentication;

public interface JwtTokenPort {
    TokenInfo generateToken(Authentication authentication);
    String getStaffNumberFromToken(String token);
    boolean validateToken(String token);
    boolean validateRefreshToken(String refreshToken);
    String getUsernameFromExpiredToken(String token);
    long getRemainingExpiration(String token);
}
