package com.example.wms.user.adapter.out;

import com.example.wms.infrastructure.mapper.UserMapper;
import com.example.wms.user.application.domain.User;
import com.example.wms.user.application.port.out.UserPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAdapter implements UserPort {
    private final UserMapper userMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String REFRESH_TOKEN_KEY_PREFIX = "refresh_token:";

    @Override
    public void deleteUser(String staffNumber) {
        log.info("[Refresh Token 삭제] Redis에서 Refresh Token 삭제 요청. staffNumber: {}", staffNumber);
        // Redis에서 Refresh Token 삭제
        redisTemplate.delete(REFRESH_TOKEN_KEY_PREFIX + staffNumber);
        log.info("[Refresh Token 삭제] Refresh Token 삭제 완료.");

        // userMapper를 통해 사용자 정보 삭제
        userMapper.deleteByStaffNumber(staffNumber);
    }

    @Override
    public Optional<User> findByStaffNumber(String staffNumber) {
        return userMapper.findByStaffNumber(staffNumber);
    }

    @Override
    public Page<User> findAllUsers(Pageable pageable) {
        List<User> users = userMapper.findAllUsers(pageable.getPageSize(), pageable.getOffset());
        long totalUsers = userMapper.countTotalUsers();
        return new PageImpl<>(users, pageable, totalUsers);
    }

    @Override
    public boolean existsById(Long userId) {
        return userMapper.existsById(userId);
    }

    @Override
    public boolean existsByStaffNumber(String staffNumber) {
        return userMapper.existsByStaffNumber(staffNumber);
    }

    @Override
    public long countTotalUsers() {
        return userMapper.countTotalUsers();
    }
}