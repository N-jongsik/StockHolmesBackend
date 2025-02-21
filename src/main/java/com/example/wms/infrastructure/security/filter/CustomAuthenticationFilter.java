package com.example.wms.infrastructure.security.filter;

import com.example.wms.infrastructure.dto.ResponseDto;
import com.example.wms.infrastructure.exception.DuplicatedException;
import com.example.wms.infrastructure.exception.TokenException;
import com.example.wms.infrastructure.jwt.JwtTokenProvider;
import com.example.wms.infrastructure.jwt.dto.Token;
import com.example.wms.infrastructure.jwt.enums.JwtHeaderUtil;
import com.example.wms.infrastructure.jwt.enums.JwtResponseMessage;
import com.example.wms.infrastructure.jwt.exception.MalformedHeaderException;
import com.example.wms.infrastructure.jwt.exception.TokenNotFoundException;
import com.example.wms.infrastructure.repository.LogoutAccessTokenRedisRepository;
import com.example.wms.user.adapter.in.dto.response.TokenInfo;
import com.example.wms.user.application.domain.RefreshToken;
import com.example.wms.user.application.service.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.wms.infrastructure.enums.ExceptionMessage.ALREADY_LOGGED_OUT;
import static com.example.wms.infrastructure.jwt.enums.JwtExceptionMessage.MALFORMED_HEADER;
import static com.example.wms.infrastructure.jwt.enums.JwtExceptionMessage.TOKEN_NOTFOUND;
import static com.example.wms.infrastructure.jwt.enums.TokenType.ACCESS;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
    private static final String UTF_8 = "utf-8";
    @Value("${jwt.cookieName}")
    private String COOKIE_NAME;

    @Value("${jwt.refresh-expired-in}")
    private long REFRESH_TOKEN_EXPIRED_IN;

    /**
     * 허용 URL 경로 배열
     */
    private static final String[] PERMIT_URLS = {
            "/api/auth/register",
            "/api/auth/login",
            "/api/user/reissue-token",
            "/api/user",
            "/api/user/list",
            "/api/upload",
            "/api/outbound",
            "/api/bin",
            "/api/swagger-ui",
            "/api/v3/api-docs",
            "/api/product",
            "/api/sse",
            "/api/outboundAssign",
            "/api/supplier",
            "/api/outboundPicking",
            "/api/outboundPacking",
            "/api/outboundLoading",
            "/api/pdf/generate",
            "/api/order",
            "/api/inbound",
            "/api/dashboard",
            "/api/worker/outbound"
    };

    /**
     * doFilterInternal 메서드는 HTTP 요청에 포함된 Access Token을 검증하고 인증을 설정합니다.
     * 만료된 경우 Redis에 저장된 Refresh Token을 통해 새로운 Access Token을 재발급합니다.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();

        log.info("🚀 요청 URI: {}", requestUri);
        // 허용된 URL 경로인지 확인
        if (isPermitUrl(requestUri)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Token token = resolveAccessToken(request);
            // 로그아웃 상태인지 확인
            checkLogout(token.getToken());

            // Access Token이 유효한 경우 SecurityContext에 인증 정보를 설정합니다.
            if (token != null && jwtTokenProvider.validateToken(token.getToken())) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token.getToken());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else if (token != null && !jwtTokenProvider.validateToken(token.getToken())) {
                handleExpiredAccessToken(request, response);
                return;
            }

            filterChain.doFilter(request, response);
        } catch (TokenException e) {
            makeTokenExceptionResponse(response, e);
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * 요청 URI가 허용된 URL 경로에 해당하는지 확인하는 메서드
     */
    private boolean isPermitUrl(String requestUri) {
        for (String url : PERMIT_URLS) {
            if (requestUri.startsWith(url)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 로그아웃 상태인지 Redis를 통해 확인합니다.
     */
    private void checkLogout(String accessToken) {
        // accessToken에서 사번 추출
        String staffNumber = jwtTokenProvider.getUsernameFromExpiredToken(accessToken);

        // Redis에서 사번으로 로그아웃 상태 확인
        boolean isLogout = logoutAccessTokenRedisRepository.existsByStaffNumber(staffNumber);
        log.info("로그아웃 상태 확인 - 이메일: {}, Redis에 존재 여부: {}", staffNumber, isLogout);

        if (isLogout) {
            throw new DuplicatedException(ALREADY_LOGGED_OUT.getMessage());
        }
    }

    /**
     * Access Token이 만료된 경우 Redis에 저장된 Refresh Token을 통해 새로운 Access Token을 발급합니다.
     */
    private void handleExpiredAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = getRefreshTokenFromRedis(request);

        if (refreshToken != null && jwtTokenProvider.validateRefreshToken(refreshToken)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
            TokenInfo tokenInfo = reissueTokensAndSaveOnRedis(authentication);
            makeTokenInfoResponse(response, tokenInfo);
        } else {
            throw new TokenNotFoundException(TOKEN_NOTFOUND.getMessage());
        }
    }

    /**
     * Authorization 헤더에서 Access Token을 추출하여 반환합니다.
     * 헤더가 유효하지 않은 형식일 경우 예외를 발생시킵니다.
     */
    private Token resolveAccessToken(HttpServletRequest request) {
        String token = request.getHeader(JwtHeaderUtil.AUTHORIZATION.getValue());
        if (StringUtils.hasText(token) && token.startsWith(JwtHeaderUtil.GRANT_TYPE.getValue())) {
            return Token.builder()
                    .tokenType(ACCESS)
                    .token(token.substring(JwtHeaderUtil.GRANT_TYPE.getValue().length()))
                    .build();
        }
        throw new MalformedHeaderException(MALFORMED_HEADER.getMessage());
    }

    /**
     * Redis에서 저장된 Refresh Token을 가져옵니다.
     * Access Token이 만료된 경우에만 사용됩니다.
     */
    private String getRefreshTokenFromRedis(HttpServletRequest request) {
        String username = jwtTokenProvider.getUsernameFromExpiredToken(request.getHeader(JwtHeaderUtil.AUTHORIZATION.getValue()));
        RefreshToken storedRefreshToken = refreshTokenService.findRefreshToken(username);

        if (storedRefreshToken == null || !StringUtils.hasText(storedRefreshToken.getRefreshToken())) {
            throw new TokenNotFoundException(TOKEN_NOTFOUND.getMessage());
        }
        return storedRefreshToken.getRefreshToken();
    }

    /**
     * TokenException 발생 시 예외 메시지를 담아 클라이언트에 응답을 반환합니다.
     */
    private void makeTokenExceptionResponse(HttpServletResponse response, TokenException e) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding(UTF_8);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.getWriter().write(
                objectMapper.writeValueAsString(
                        ResponseDto.create(e.getMessage())
                )
        );
    }

    /**
     * 새로운 Access Token을 발급하고 Redis에 Refresh Token을 갱신하여 저장합니다.
     */
    private TokenInfo reissueTokensAndSaveOnRedis(Authentication authentication) {
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        refreshTokenService.saveRefreshToken(authentication.getName(), tokenInfo.getRefreshToken());
        return tokenInfo;
    }

    /**
     * 새로 발급된 Access Token을 클라이언트에게 응답합니다.
     * 메시지는 JwtResponseMessage.TOKEN_REISSUED에 정의된 메시지를 사용합니다.
     */
    private void makeTokenInfoResponse(HttpServletResponse response, TokenInfo tokenInfo) throws IOException {
        response.setStatus(HttpStatus.CREATED.value());
        response.setCharacterEncoding(UTF_8);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.getWriter().write(
                objectMapper.writeValueAsString(
                        ResponseDto.create(JwtResponseMessage.TOKEN_REISSUED.getMessage(), tokenInfo)
                )
        );
    }
}