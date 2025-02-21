package com.example.wms.infrastructure.security.util;

import com.example.wms.infrastructure.mapper.UserMapper;
import com.example.wms.user.application.domain.User;
import com.example.wms.user.application.domain.enums.UserExceptionMessage;
import com.example.wms.user.application.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.example.wms.infrastructure.enums.ExceptionMessage.NOT_FOUND_LOGIN_USER;


@Component
@RequiredArgsConstructor
@Slf4j  // 로깅을 위해 추가
public class SecurityUtils {
    private final UserMapper userMapper;

    public static String getLoginUserStaffNumber() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Current authentication: {}", authentication);  // 로그 추가
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            log.error("No authenticated user found");  // 로그 추가
            throw new IllegalStateException(NOT_FOUND_LOGIN_USER.getMessage());
        }
        return authentication.getName();
    }

    public User getLoginUser() {
        String staffNumber = getLoginUserStaffNumber();
        log.info("Attempting to find user with staff number: {}", staffNumber);  // 로그 추가
        return userMapper.findByStaffNumber(staffNumber)
                .orElseThrow(() -> {
                    log.error("User not found for staff number: {}", staffNumber);  // 로그 추가
                    return new UserNotFoundException(UserExceptionMessage.USER_NOT_FOUND.getMessage());
                });
    }
}