package com.example.wms.user.adapter.out;

import com.example.wms.infrastructure.mapper.UserMapper;
import com.example.wms.user.application.domain.User;
import com.example.wms.user.application.port.out.UserQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserQueryAdapter implements UserQueryPort {

    private final UserMapper userMapper;

    @Override
    public boolean existsByStaffNumber(String staffNumber) {
        return userMapper.findByStaffNumber(staffNumber).isPresent();
    }

    @Override
    public Optional<User> findByStaffNumber(String staffNumber) {
        return userMapper.findByStaffNumber(staffNumber);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userMapper.findById(userId);
    }

    @Override
    public boolean existsById(Long userId) {
        return userMapper.existsById(userId);
    }

    @Override
    public String findLastStaffNumberByRole(String role) {
        return userMapper.findLastStaffNumberByRole(role);
    }
}
