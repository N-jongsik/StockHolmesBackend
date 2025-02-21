package com.example.wms.user.application.port.out;

import com.example.wms.user.application.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserQueryPort {
    boolean existsByStaffNumber(String staffNumber);
    Optional<User> findByStaffNumber(String staffNumber);
    String findLastStaffNumberByRole(String prefix);
    Optional<User> findById(Long userId);
    boolean existsById(Long userId);

}
