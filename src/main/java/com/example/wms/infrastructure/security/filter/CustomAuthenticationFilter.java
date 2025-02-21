package com.example.wms.infrastructure.security.filter;

import com.example.wms.infrastructure.dto.ResponseDto;
import com.example.wms.infrastructure.exception.DuplicatedException;
import com.example.wms.infrastructure.exception.TokenException;
import com.example.wms.infrastructure.jwt.JwtTokenProvider;
import com.example.wms.infrastructure.jwt.dto.Token;
import com.example.wms.infrastructure.jwt.enums.JwtHeaderUtil;
import com.example.wms.infrastructure.jwt.enums.JwtResponseMessage;
import com.example.wms.infrastructure.jwt.exception.TokenNotFoundException;
import com.example.wms.infrastructure.repository.LogoutAccessTokenRedisRepository;
import com.example.wms.user.adapter.in.dto.response.TokenInfo;
import com.example.wms.user.application.domain.RefreshToken;
import com.example.wms.user.application.service.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.wms.infrastructure.enums.ExceptionMessage.ALREADY_LOGGED_OUT;
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

    private static final String[] PERMIT_URLS = {
            "/api/auth/register",
            "/api/auth/login",
            "/api/user/reissue-token",
            "/api/user/list",
            "/api/user/staff-number",
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
            "/api/inbound",
            "/api/dashboard"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        log.info("🚀 요청 URI: {}", requestUri);

        // 허용된 URL이면 바로 통과
        if (isPermitUrl(requestUri)) {
            log.info("Permitting access to URL without authentication: {}", requestUri);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Token token = resolveAccessToken(request);

            // 토큰이 null이면 다음 필터로 진행
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            checkLogout(token.getToken());

            if (jwtTokenProvider.validateToken(token.getToken())) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token.getToken());
                log.info("Valid token. Setting authentication: {}", authentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.info("Invalid token. Handling expired access token.");
                handleExpiredAccessToken(request, response);
                return;
            }

            filterChain.doFilter(request, response);
        } catch (TokenException e) {
            log.error("Token exception occurred: ", e);
            makeTokenExceptionResponse(response, e);
        }
    }

    private Token resolveAccessToken(HttpServletRequest request) {
        String token = request.getHeader(JwtHeaderUtil.AUTHORIZATION.getValue());

        // 토큰이 없거나 형식에 맞지 않으면 null 반환
        if (!StringUtils.hasText(token) || !token.startsWith(JwtHeaderUtil.GRANT_TYPE.getValue())) {
            return null;
        }

        return Token.builder()
                .tokenType(ACCESS)
                .token(token.substring(JwtHeaderUtil.GRANT_TYPE.getValue().length()))
                .build();
    }

    private boolean isPermitUrl(String requestUri) {
        for (String url : PERMIT_URLS) {
            if (requestUri.startsWith(url)) {
                return true;
            }
        }
        return false;
    }

    private void checkLogout(String accessToken) {
        String staffNumber = jwtTokenProvider.getUsernameFromExpiredToken(accessToken);
        boolean isLogout = logoutAccessTokenRedisRepository.existsByStaffNumber(staffNumber);
        log.info("로그아웃 상태 확인 - 이메일: {}, Redis에 존재 여부: {}", staffNumber, isLogout);

        if (isLogout) {
            throw new DuplicatedException(ALREADY_LOGGED_OUT.getMessage());
        }
    }

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

    private String getRefreshTokenFromRedis(HttpServletRequest request) {
        String username = jwtTokenProvider.getUsernameFromExpiredToken(request.getHeader(JwtHeaderUtil.AUTHORIZATION.getValue()));
        RefreshToken storedRefreshToken = refreshTokenService.findRefreshToken(username);

        if (storedRefreshToken == null || !StringUtils.hasText(storedRefreshToken.getRefreshToken())) {
            throw new TokenNotFoundException(TOKEN_NOTFOUND.getMessage());
        }
        return storedRefreshToken.getRefreshToken();
    }

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

    private TokenInfo reissueTokensAndSaveOnRedis(Authentication authentication) {
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        refreshTokenService.saveRefreshToken(authentication.getName(), tokenInfo.getRefreshToken());
        return tokenInfo;
    }

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