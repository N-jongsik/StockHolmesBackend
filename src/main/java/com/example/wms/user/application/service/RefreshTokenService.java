package com.example.wms.user.application.service;

import com.example.wms.infrastructure.exception.NotFoundException;
import com.example.wms.infrastructure.exception.TokenException;
import com.example.wms.infrastructure.jwt.exception.ExpiredTokenException;
import com.example.wms.user.application.domain.RefreshToken;
import com.example.wms.user.application.port.in.RefreshTokenUseCase;
import com.example.wms.user.application.port.out.JwtTokenPort;
import com.example.wms.user.application.port.out.RefreshTokenPort;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.wms.user.application.domain.enums.UserExceptionMessage.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService implements RefreshTokenUseCase {

    private final RefreshTokenPort refreshTokenPort;
    private final JwtTokenPort jwtTokenPort;


    @Value("${jwt.refresh-expired-in}")
    private long REFRESH_TOKEN_EXPIRED_IN;

    // 리프레시 토큰을 저장
    @Override
    public void saveRefreshToken(String staffNumber, String refreshToken) {
        RefreshToken token = RefreshToken.builder()
                .staffNumber(staffNumber)
                .refreshToken(refreshToken)
                .expiration(REFRESH_TOKEN_EXPIRED_IN)
                .build();

        refreshTokenPort.save(token);

        log.info("[Refresh Token 저장] Refresh Token 저장 완료.");
    }

    // 리프레시 토큰을 삭제
    @Override
    @Transactional
    public void deleteByStaffNumber(String staffNumber) {
        refreshTokenPort.deleteByStaffNumber(staffNumber);
        log.info("[Refresh Token 삭제] Refresh Token 삭제 완료. 사번: {}", staffNumber);
    }

    // 리프레시 토큰을 유효성 검증
    @Override
    public void validateRefreshToken(String refreshToken, String staffNumber) {
        log.info("[Refresh Token 검증] Refresh Token 유효성 검사 시작. 사번: {}", staffNumber);

        if (!jwtTokenPort.validateRefreshToken(refreshToken)) {
            log.error("[Refresh Token 검증] Refresh Token 만료됨.");
            throw new ExpiredTokenException(REFRESH_TOKEN_EXPIRED.getMessage());
        }

        String storedRefreshToken = findRefreshToken(staffNumber).getRefreshToken();
        log.info("[Refresh Token 검증] Redis에 저장된 Refresh Token과 비교.");

        if (!storedRefreshToken.equals(refreshToken)) {
            log.error("[Refresh Token 검증] 토큰 불일치. 저장된 토큰과 일치하지 않음.");
            throw new TokenException(TOKEN_MISMATCH.getMessage());
        }

        log.info("[Refresh Token 검증] Refresh Token 검증 성공.");
    }

    // 사번으로 저장된 리프레시 토큰 찾기
    @Override
    public RefreshToken findRefreshToken(String staffNumber) {
        log.info("[Refresh Token 검색] 이메일에 대한 Refresh Token 검색. 사번: {}", staffNumber);
        return refreshTokenPort.findByStaffNumber(staffNumber)
                .orElseThrow(() -> {
                    log.error("[Refresh Token 검색] 해당 이메일에 대한 Refresh Token 없음.");
                    return new NotFoundException(REFRESH_TOKEN_NOT_FOUND.getMessage());
                });
    }

    // 리프레시 토큰을 검증하고 이메일 반환
    public String validateAndGetStaffNumber(String refreshToken) {
        try {
            return jwtTokenPort.getUsernameFromExpiredToken(refreshToken);
        } catch (ExpiredJwtException e) {
            log.error("[토큰 재발급] 리프레시 토큰이 만료되었습니다. 재로그인 필요.");
            throw new TokenException(REFRESH_TOKEN_EXPIRED.getMessage());
        }
    }

    // 리프레시 토큰이 이메일과 일치하는지 확인
    public void checkRefreshToken(String refreshToken, String staffNumber) {
        String storedToken = findRefreshToken(staffNumber).getRefreshToken();

        if (!storedToken.equals(refreshToken)) {
            log.error("[토큰 불일치] 토큰 불일치로 재발급 실패. 사번: {}", staffNumber);
            throw new TokenException(TOKEN_MISMATCH.getMessage());
        }
    }
}
