package com.example.wms.infrastructure.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.wms.infrastructure.enums.ExceptionMessage;
import com.example.wms.infrastructure.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private static final String UTF_8 = "utf-8";

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8);

        response.getWriter().write(
                objectMapper.writeValueAsString(
                        ResponseDto.create(ExceptionMessage.AUTHENTICATION_FAILED.getMessage())
                )
        );
    }
}