package com.example.wms.user.adapter.in;

import com.example.wms.infrastructure.jwt.enums.JwtHeaderUtil;
import com.example.wms.user.adapter.in.dto.request.LoginReqDto;
import com.example.wms.user.adapter.in.dto.request.SignUpReqDto;
import com.example.wms.user.adapter.in.dto.response.AuthenticatedResDto;
import com.example.wms.user.adapter.in.dto.response.TokenInfo;
import com.example.wms.user.adapter.in.dto.response.UserInfoResDto;
import com.example.wms.user.application.port.in.AuthUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "auth", description = "auth domain apis")
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUseCase authUseCase;

    @Value("${jwt.cookieName}")
    private String COOKIE_NAME;

    @Value("${jwt.refresh-expired-in}")
    private long REFRESH_TOKEN_EXPIRED_IN;

    /**
     * 회원가입 처리
     *
     * @param signUpReqDto 회원가입 요청 데이터
     * @return 성공 시 HTTP 201 상태 반환
     */
    @Operation(summary = "유저 등록", description = "필요한 정보를 입력하여 유저를 등록합니다.")
    @PostMapping("/register")
    public ResponseEntity<UserInfoResDto> addUser(@RequestBody @Valid SignUpReqDto signUpReqDto) {

        AuthenticatedResDto authenticatedResDto = authUseCase.signUp(signUpReqDto);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + authenticatedResDto.getTokenInfo().getAccessToken());

        ResponseCookie refreshTokenCookie = ResponseCookie.from(COOKIE_NAME, authenticatedResDto.getTokenInfo().getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(REFRESH_TOKEN_EXPIRED_IN)
                .sameSite("Lax")
                .build();
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(headers)
                .body(authenticatedResDto.getUserInfo());
    }

    /**
     * 회원 로그인
     * Access Token을 헤더에, Refresh Token을 HttpOnly 쿠키에 설정
     *
     * @param loginReqDto 로그인 요청 데이터
     * @return 로그인 결과
     */
    @Operation(summary = "로그인", description = "일반 로그인합니다.")
    @PostMapping("/login")
    public ResponseEntity<UserInfoResDto> login(@RequestBody LoginReqDto loginReqDto) {

        AuthenticatedResDto authenticatedResDto = authUseCase.login(loginReqDto);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + authenticatedResDto.getTokenInfo().getAccessToken());

        ResponseCookie refreshTokenCookie = ResponseCookie.from(COOKIE_NAME, authenticatedResDto.getTokenInfo().getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(REFRESH_TOKEN_EXPIRED_IN)
                .sameSite("Lax")
                .build();
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return ResponseEntity.ok()
                .headers(headers)
                .body(authenticatedResDto.getUserInfo());
    }

    /**
     * 토큰 재발급
     * 새로운 Access Token을 헤더에, Refresh Token을 HttpOnly 쿠키에 설정
     */
    @Operation(summary = "토큰 재발급", description = "JWT 토큰을 재발급합니다.")
    @PostMapping("/reissue-token")
    public ResponseEntity<Void> reissueToken(@CookieValue(name = "refreshToken") String refreshToken) {
        TokenInfo newTokenInfo = authUseCase.reissueToken(refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, JwtHeaderUtil.GRANT_TYPE.getValue() + " " + newTokenInfo.getAccessToken());

        ResponseCookie newRefreshTokenCookie = ResponseCookie.from(COOKIE_NAME, newTokenInfo.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(REFRESH_TOKEN_EXPIRED_IN)
                .sameSite("Lax")
                .build();
        headers.add(HttpHeaders.SET_COOKIE, newRefreshTokenCookie.toString());

        return ResponseEntity.ok().headers(headers).build();
    }

    /**
     * 로그아웃 처리
     * Access 및 Refresh 토큰 무효화
     */
    @Operation(summary = "로그아웃", description = "로그아웃 합니다.")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String accessToken) {
        authUseCase.logout(JwtHeaderUtil.extractToken(accessToken));
        return ResponseEntity.ok().build();
    }
}
