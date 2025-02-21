package com.example.wms.notification.application.domain;


import com.example.wms.user.application.domain.enums.UserRole;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    private Long notificationId; // 알림 ID

    private String content; // 채팅 내용

    private String event;
    // 1.출고예정, 2.발주신청, 3.발주승인, 4.입하심사, 5.입고적치, 6.출고지시, 7.출고피킹, 8.출고패킹, 9.출하상차

    private UserRole userRole; // 사용자 유형
}
