package com.example.wms.user.adapter.out;

import com.example.wms.infrastructure.mapper.UserMapper;
import com.example.wms.user.application.domain.User;
import com.example.wms.user.application.domain.enums.UserExceptionMessage;
import com.example.wms.user.application.exception.InvalidSignUpException;
import com.example.wms.user.application.port.out.AuthPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthAdapter implements AuthPort {

    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        int result = userMapper.save(user);
        if (result > 0) {
            return user;
        } else {
            throw new InvalidSignUpException("회원가입 실패");
        }
    }

    @Override
    public User findByStaffNumber(String staffNumber) {
        return userMapper.findByStaffNumber(staffNumber)
                .orElseThrow(() -> new InvalidSignUpException(UserExceptionMessage.USER_NOT_FOUND.getMessage()));

    }

    @Override
    public boolean isStaffNumberDuplicated(String email) {
        return userMapper.existsByStaffNumber(email);
    }
}
