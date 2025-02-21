package com.example.wms.infrastructure.security.service;

import com.example.wms.infrastructure.mapper.UserMapper;
import com.example.wms.user.application.domain.enums.UserExceptionMessage;
import com.example.wms.user.application.domain.User;
import com.example.wms.user.application.exception.UserNotFoundException;
import com.example.wms.infrastructure.security.domain.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j  // 로깅을 위해 추가
public class CustomUserDetailsService implements UserDetailsService {
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Attempting to load user details for username: {}", username);  // 로그 추가
        User user = userMapper.findByStaffNumber(username)
                .orElseThrow(() -> {
                    log.error("User not found for username: {}", username);  // 로그 추가
                    return new UserNotFoundException(UserExceptionMessage.USER_NOT_FOUND.getMessage());
                });
        return new CustomUserDetails(user);
    }
}
