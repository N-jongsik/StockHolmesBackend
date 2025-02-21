package com.example.wms.user.application.port.out;

import com.example.wms.user.application.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserPort {
    void deleteUser(String staffNumber); // 회원 탈퇴
    Optional<User> findByStaffNumber(String staffNumber); // 정보조회
    Page<User> findAllUsers(Pageable pageable);
    boolean existsById(Long userId);
    boolean existsByStaffNumber(String staffNumber);
    long countTotalUsers();
}