package com.example.wms.user.application.domain;

import com.example.wms.infrastructure.entity.BaseEntity;
import com.example.wms.user.application.domain.enums.UserRole;
import lombok.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
    private Long userId;        // 사용자 고유 ID
    private String name;        // 사용자 이름
    private String profileImage; // 프로필 이미지 URL
    private String staffNumber; // 사번
    private String password; // 비밀번호
    private String phone; // 전화번호
    private String gender;      // 성별
    private Boolean isActive;   // 활성화 여부
    private String address;     // 주소
    private UserRole userRole;    // 사용자 유형 (예: 일반 사용자, 관리자)
    private String birthDate;   // 사용자 생년월일
    private Long supplierId;    // 공급업체 ID (nullable)

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateActiveStatus(boolean isActive) {
        this.isActive = isActive;
    }

    public void updatePUserRole(UserRole newRole) {
        this.userRole = newRole;
    }
}
