package com.example.wms.user.application.domain.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    ROLE_USER("일반", "US"),
    ROLE_SUPPLIER("공급업체", "SP"),
    ROLE_WORKER("작업자", "WO"),
    ROLE_ADMIN("관리자", "AD");

    private final String value;
    private final String staffNumberPrefix;

    UserRole(String value, String staffNumberPrefix) {
        this.value = value;
        this.staffNumberPrefix = staffNumberPrefix;
    }

    public static UserRole getUserRole(String value) {
        // 문자열로 받은 역할을 Enum으로 변환
        switch (value) {
            case "관리자":
                return ROLE_ADMIN;
            case "공급업체":
                return ROLE_SUPPLIER;
            case "작업자":
                return ROLE_WORKER;
            default:
                return ROLE_USER;
        }
    }

    public static UserRole fromString(String role) {
        try {
            return UserRole.valueOf(role);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
