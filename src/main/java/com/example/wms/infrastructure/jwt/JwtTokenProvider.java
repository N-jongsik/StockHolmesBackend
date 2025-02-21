package com.example.wms.infrastructure.jwt;

import com.example.wms.user.adapter.in.dto.response.TokenInfo;
import com.example.wms.infrastructure.exception.TokenException;
import com.example.wms.infrastructure.jwt.enums.JwtHeaderUtil;
import com.example.wms.infrastructure.jwt.exception.ExpiredTokenException;
import com.example.wms.infrastructure.jwt.exception.InvalidTokenException;
import com.example.wms.user.application.port.out.JwtTokenPort;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static com.example.wms.infrastructure.jwt.enums.JwtExceptionMessage.EXPIRED_TOKEN;
import static com.example.wms.infrastructure.jwt.enums.JwtExceptionMessage.INVALID_TOKEN;

@Slf4j
@Component
public class JwtTokenProvider implements JwtTokenPort {

    private final Key key;
    private final long ACCESS_TOKEN_EXPIRED_IN;
    private final long REFRESH_TOKEN_EXPIRED_IN;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-expired-in}") long accessTokenExpiredIn,
            @Value("${jwt.refresh-expired-in}") long refreshTokenExpiredIn) {
        this.ACCESS_TOKEN_EXPIRED_IN = accessTokenExpiredIn;
        this.REFRESH_TOKEN_EXPIRED_IN = refreshTokenExpiredIn;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 토큰에서 사번을 추출합니다.
     */
    public String getStaffNumberFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    /**
     * Access Token과 Refresh Token을 생성하여 TokenInfo 객체로 반환합니다.
     */
    public TokenInfo generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRED_IN);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRED_IN);
        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenInfo.builder()
                .grantType(JwtHeaderUtil.GRANT_TYPE.getValue().trim())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Access Token에서 Authentication 객체를 생성하여 반환합니다.
     */
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new InvalidTokenException(INVALID_TOKEN.getMessage());
        }

        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * 토큰이 유효한지 검증합니다.
     */
    public boolean validateToken(String token) throws TokenException {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException |
                 IllegalArgumentException e) {
            throw new InvalidTokenException(INVALID_TOKEN.getMessage());
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException(EXPIRED_TOKEN.getMessage());
        }
    }

    /**
     * Refresh Token의 유효성을 검증합니다.
     */
    public boolean validateRefreshToken(String refreshToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // 유효하지 않으면 false 반환
        }
    }

    /**
     * 만료된 Access Token에서 사용자 이름을 추출합니다.
     */
    public String getUsernameFromExpiredToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject(); // 만료된 토큰에서 subject를 가져옴
        }
    }

    /**
     * Claims 객체를 파싱하여 반환합니다. 만료된 토큰도 파싱하여 예외를 던집니다.
     */
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException(EXPIRED_TOKEN.getMessage());
        }
    }

    /**
     * 토큰의 남은 만료 시간을 반환합니다.
     */
    public long getRemainingExpiration(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            Date expiration = claims.getExpiration();
            long now = (new Date()).getTime();
            return expiration.getTime() - now; // 남은 시간
        } catch (ExpiredJwtException e) {
            return 0; // 만료된 경우 남은 시간을 0으로 반환
        }
    }
}
