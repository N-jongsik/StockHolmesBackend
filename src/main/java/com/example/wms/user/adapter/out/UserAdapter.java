package com.example.wms.user.adapter.out;

import com.example.wms.infrastructure.mapper.UserMapper;
import com.example.wms.user.application.domain.User;
import com.example.wms.user.application.domain.enums.UserExceptionMessage;
import com.example.wms.user.application.exception.InvalidSignUpException;
import com.example.wms.user.application.port.out.UserPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public User findByStaffNumber(String staffNumber) {
        return userMapper.findByStaffNumber(staffNumber)
                .orElseThrow(() -> new InvalidSignUpException(UserExceptionMessage.USER_NOT_FOUND.getMessage()));
    }

    @Override
    public List<User> findAllUsers(int limit, int offset) {
        return userMapper.findAllUsers(limit, offset);
    }

    // 활성화/비활성화 등록
}
