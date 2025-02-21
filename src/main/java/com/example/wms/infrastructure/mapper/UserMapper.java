package com.example.wms.infrastructure.mapper;

import com.example.wms.user.application.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {

    // 회원가입
    int save(@Param("user") User user);

    // 직원 번호로 사용자 조회
    Optional<User> findByStaffNumber(@Param("staffNumber") String staffNumber);

    // 직원 번호로 이미 존재하는지 체크
    boolean existsByStaffNumber(@Param("staffNumber") String staffNumber);

    // 직원 번호로 사용자 삭제
    void deleteByStaffNumber(@Param("staffNumber") String staffNumber);

    // 사용자 정보 업데이트
    void updateUser(@Param("user") User user);

    // 사용자 ID로 조회
    Optional<User> findById(@Param("userId") Long userId);

    // 사용자 ID로 존재 여부 체크
    boolean existsById(@Param("userId") Long userId);

    // 모든 사용자 조회
    List<User> findAllUsers(@Param("limit") int limit, @Param("offset") int offset);

    // 역할별 마지막 사번 조회
    String findLastStaffNumberByRole(@Param("prefix") String prefix);


}
