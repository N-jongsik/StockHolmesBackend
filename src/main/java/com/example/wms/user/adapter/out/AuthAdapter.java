package com.example.wms.user.adapter.out;

import com.example.wms.infrastructure.mapper.UserMapper;
import com.example.wms.user.application.domain.User;
import com.example.wms.user.application.exception.InvalidSignUpException;
import com.example.wms.user.application.port.out.AuthPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
    public Optional<User> findByStaffNumber(String staffNumber) {
        return userMapper.findByStaffNumber(staffNumber);
    }

    @Override
    public boolean existsByStaffNumber(String staffNumber) {
        return userMapper.existsByStaffNumber(staffNumber);
    }

    @Override
    public String findLastStaffNumberByRole(String role) {
        return userMapper.findLastStaffNumberByRole(role);
    }

    @Override
    public boolean isStaffNumberDuplicated(String staffNumber) {
        return userMapper.existsByStaffNumber(staffNumber);
    }

    @Override
    public void updateUserRole(String staffNumber, String newRole) {
        userMapper.updateUserRole(staffNumber, newRole);
    }

    @Override
    public void updateUserActive(String staffNumber, boolean isActive) {
        userMapper.updateUserActive(staffNumber, isActive);
    }

    @Override
    public void updateUserPassword(String staffNumber, String encodedPassword) {
        userMapper.updateUserPassword(staffNumber, encodedPassword);
    }
}