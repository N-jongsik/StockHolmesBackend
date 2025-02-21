package com.example.wms.user.application.service;

import com.example.wms.infrastructure.exception.DuplicatedException;
import com.example.wms.infrastructure.repository.LogoutAccessTokenRedisRepository;
import com.example.wms.user.adapter.in.dto.request.LoginReqDto;
import com.example.wms.user.adapter.in.dto.request.SignUpReqDto;
import com.example.wms.user.adapter.in.dto.response.AuthenticatedResDto;
import com.example.wms.user.adapter.in.dto.response.TokenInfo;
import com.example.wms.user.adapter.in.dto.response.UserInfoResDto;
import com.example.wms.user.application.domain.LogoutAccessToken;
import com.example.wms.user.application.domain.User;
import com.example.wms.user.application.domain.enums.UserRole;
import com.example.wms.user.application.exception.UserNotFoundException;
import com.example.wms.user.application.port.in.AuthUseCase;
import com.example.wms.user.application.port.out.AuthPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static com.example.wms.infrastructure.security.util.SecurityUtils.getLoginUserStaffNumber;
import static com.example.wms.user.application.domain.enums.UserExceptionMessage.DUPLICATED_STAFF_NUMBER;
import static com.example.wms.user.application.domain.enums.UserExceptionMessage.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements AuthUseCase {

    private final AuthPort authPort;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenService refreshTokenService;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService; // UserService를 주입받음

    @Override
    public AuthenticatedResDto signUp(SignUpReqDto signUpReqDto) {
        UserRole userRole = UserRole.getUserRole(signUpReqDto.getUserRole());

        // 사번 자동 생성
        String staffNumber = userService.generateStaffNumber(userRole);

        if (authPort.existsByStaffNumber(staffNumber)) {
            throw new DuplicatedException(DUPLICATED_STAFF_NUMBER.getMessage());
        }

        signUpReqDto.setPassword(passwordEncoder.encode(signUpReqDto.getBirthDate()));
        log.info("[회원가입] 패스워드 암호화 완료.");

        signUpReqDto.setStaffNumber(staffNumber);

        User user = authPort.save(signUpReqDto.dtoToEntity());

        TokenInfo tokenInfo = jwtTokenService.generateAndSaveTokens(staffNumber, signUpReqDto.getBirthDate());

        log.info("[회원가입 성공] 사번: {}", user.getStaffNumber());

        return AuthenticatedResDto.builder()
                .userInfo(UserInfoResDto.entityToResDto(user))
                .tokenInfo(tokenInfo)
                .build();
    }

    @Override
    public AuthenticatedResDto login(LoginReqDto loginReqDto) {
        TokenInfo tokenInfo = jwtTokenService.generateAndSaveTokens(loginReqDto.getStaffNumber(), loginReqDto.getPassword());

        User user = authPort.findByStaffNumber(loginReqDto.getStaffNumber())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        // SecurityContextHolder에 인증 정보 저장
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user.getStaffNumber(), null, Collections.singletonList(new SimpleGrantedAuthority(user.getUserRole().name()))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        logoutAccessTokenRedisRepository.deleteByStaffNumber(loginReqDto.getStaffNumber());
        refreshTokenService.saveRefreshToken(user.getStaffNumber(), tokenInfo.getRefreshToken());

        log.info("[로그인 성공] 사번: {}", loginReqDto.getStaffNumber());

        return AuthenticatedResDto.builder()
                .userInfo(UserInfoResDto.entityToResDto(user))
                .tokenInfo(tokenInfo)
                .build();
    }

    @Override
    public void logout(String accessToken) {
        String staffNumber = getLoginUserStaffNumber();

        long remainMilliSeconds = jwtTokenService.getRemainingExpiration(accessToken);
        jwtTokenService.invalidateAccessToken(accessToken);
        refreshTokenService.deleteByStaffNumber(staffNumber);
        logoutAccessTokenRedisRepository.save(LogoutAccessToken.builder()
                .staffNumber(staffNumber)
                .accessToken(accessToken)
                .expiration(remainMilliSeconds)
                .build());

        log.info("[로그아웃] 로그아웃 완료.");
    }

    @Override
    public TokenInfo reissueToken(String refreshToken) {
        String staffNumber = refreshTokenService.validateAndGetStaffNumber(refreshToken);

        TokenInfo newTokenInfo = jwtTokenService.generateAndSaveTokens(staffNumber, null);

        log.info("[토큰 재발급 성공] 새로운 토큰 발급: {}", newTokenInfo.getAccessToken());

        refreshTokenService.saveRefreshToken(staffNumber, newTokenInfo.getRefreshToken());
        return newTokenInfo;
    }

    @Override
    public void updateUserRole(String staffNumber, String newRole) {
        User user = authPort.findByStaffNumber(staffNumber)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        UserRole userRole = UserRole.getUserRole(newRole);

        authPort.updateUserRole(staffNumber, userRole.name());

        log.info("[사용자 역할 변경] 사번: {}, 새로운 역할: {}", staffNumber, newRole);
    }

    @Override
    public void updateActive(String staffNumber, boolean isActive) {
        User user = authPort.findByStaffNumber(staffNumber)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        authPort.updateUserActive(staffNumber, isActive);

        log.info("[사용자 활성 상태 변경] 사번: {}, 활성 상태: {}", staffNumber, isActive);
    }

    @Override
    public void updateUserPassword(String staffNumber, String newPassword) {
        User user = authPort.findByStaffNumber(staffNumber)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(newPassword);

        authPort.updateUserPassword(staffNumber, encodedPassword);

        log.info("[비밀번호 변경] 사번: {}", staffNumber);
    }

    // 비밀번호 복잡성 검증 메서드
//    private void validatePasswordComplexity(String password) {
//        if (password == null || password.length() < 8) {
//            throw new IllegalArgumentException("비밀번호는 최소 8자 이상이어야 합니다.");
//        }
//
//        boolean hasUppercase = password.matches(".*[A-Z].*");
//        boolean hasLowercase = password.matches(".*[a-z].*");
//        boolean hasDigit = password.matches(".*\\d.*");
//        boolean hasSpecialChar = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
//
//        if (!(hasUppercase && hasLowercase && hasDigit && hasSpecialChar)) {
//            throw new IllegalArgumentException("비밀번호는 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.");
//        }
//    }
}