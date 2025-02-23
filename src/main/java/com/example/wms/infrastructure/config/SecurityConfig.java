package com.example.wms.infrastructure.config;

import com.example.wms.infrastructure.jwt.JwtTokenProvider;
import com.example.wms.infrastructure.repository.LogoutAccessTokenRedisRepository;
import com.example.wms.infrastructure.security.filter.CustomAuthenticationFilter;
import com.example.wms.infrastructure.security.handler.CustomAccessDeniedHandler;
import com.example.wms.infrastructure.security.handler.CustomAuthenticationEntryPoint;
import com.example.wms.user.application.service.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(corsConfigurationSource())
                .and()

                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .authorizeRequests()

                .anyRequest().permitAll() // 필터에서 거름

                .and()
                .exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler(objectMapper))
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint(objectMapper))
                .and()
                .addFilterBefore(
                        new CustomAuthenticationFilter(refreshTokenService, jwtTokenProvider, objectMapper, logoutAccessTokenRedisRepository),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
