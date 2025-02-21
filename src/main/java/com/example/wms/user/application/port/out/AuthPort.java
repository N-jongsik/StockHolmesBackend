package com.example.wms.user.application.port.out;

import com.example.wms.user.application.domain.User;
import java.util.Optional;

public interface AuthPort {
    User save(User user);
    Optional<User> findByStaffNumber(String staffNumber);
    boolean existsByStaffNumber(String staffNumber);
    String findLastStaffNumberByRole(String role);
    boolean isStaffNumberDuplicated(String staffNumber);
    void updateUserRole(String staffNumber, String newRole);
    void updateUserActive(String staffNumber, boolean isActive);
    void updateUserPassword(String staffNumber, String encodedPassword);
}